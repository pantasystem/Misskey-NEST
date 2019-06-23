package org.panta.misskeynest.interactor

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
import okio.ByteString
import org.panta.misskeynest.entity.BodyProperty
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.StreamingProperty
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.usecase.INoteCaptureUseCase
import org.panta.misskeynest.viewdata.NoteViewData
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "NoteCaptureUseCase"
class NoteCaptureUseCase(override var mAdapterOperator: IOperationAdapter<NoteViewData>?,
                         private val mConnectionProperty: ConnectionProperty) : INoteCaptureUseCase {



    private val captureNoteMap = HashMap<String, NoteViewData>()
    private val mNoteUpdate = NoteUpdateUseCase()

    private var mWebSocket: WebSocket? = null

    private val mClient = OkHttpClient()

    //エラーが発生したときにこのQueueにcaptureNoteMapをプッシュする
    private val couldNoteBeSentDataQueue = ArrayDeque<NoteViewData>()

    override fun start() {
        val wssDomain = mConnectionProperty.domain.replace("https://", "wss://")

        val request = Request.Builder()
            .url(wssDomain)
            .build()
        mWebSocket = mClient.newWebSocket(request, w)

    }

    override fun add(viewData: NoteViewData) {

        synchronized(captureNoteMap){
            val isIncludeNote = captureNoteMap[viewData.toShowNote.id]
            if( isIncludeNote != null ){
                couldNoteBeSentDataQueue.add(viewData)
                return
            }
            captureNoteMap[viewData.toShowNote.id] = viewData
        }


        //登録処理


        if(mWebSocket == null){
            Log.d(TAG, "WebSocket が nullのため送信不能")
        }else{
            val data = StreamingProperty(type = "subNote",
                body = BodyProperty(id = viewData.toShowNote.id)
            )


            try{
                mWebSocket?.send(jacksonObjectMapper().writeValueAsString(data))
            }catch (e: IOException){
                Log.d(TAG, "送信中にエラーが発生してしまった・・", e)
            }
        }

    }

    override fun addAll(list: List<NoteViewData>) {
        list.forEach{
            add(it)
        }
    }

    override fun clear() {

        synchronized(captureNoteMap){
            val iterator = captureNoteMap.iterator()
            while(iterator.hasNext()){
                val next = iterator.next()
                iterator.remove()
                unCapture(next.value)
            }
            couldNoteBeSentDataQueue.clear()
        }


    }

    override fun remove(viewData: NoteViewData) {
        try{

            unCapture(viewData)
            synchronized(captureNoteMap){
                captureNoteMap.remove(viewData.toShowNote.id)
            }
        }catch(e: Exception){
            Log.d(TAG, "remove中にエラー発生", e)
        }
    }

    private fun unCapture(viewData: NoteViewData){
        mWebSocket?: return

        val data = StreamingProperty(type = "unsubNote",
            body = BodyProperty(id = viewData.toShowNote.id)
        )


        try{
            mWebSocket?.send(jacksonObjectMapper().writeValueAsString(data))
        }catch(e: Exception){
            Log.d(TAG, "unCapture中にエラー発生", e)
        }



    }

    override fun isActive(): Boolean {
        return mWebSocket != null
    }

    private fun onReceivedMessage(text: String){
        if( text.isBlank() ) return

        try{
            val obj: StreamingProperty = jacksonObjectMapper().readValue(text)

            val id = obj.body.id
            val userId = obj.body.body?.userId
            val isMyReaction = mConnectionProperty.userPrimaryId == userId
            val reaction = obj.body.body?.reaction

            captureNoteMap.filter{
                it.key == id
            }.forEach{
                val viewData = mAdapterOperator?.getItem(it.key)

                if(viewData != null){

                    Handler(Looper.getMainLooper()).post{

                        if(obj.body.type == "reacted"){
                            mAdapterOperator?.updateItem(mNoteUpdate.addReaction(reaction!!, viewData, isMyReaction))
                        }else if(obj.body.type == "unreacted"){
                            mAdapterOperator?.updateItem(mNoteUpdate.removeReaction(reaction!!, viewData, isMyReaction))
                        }else if(obj.body.type == "deleted"){
                            mAdapterOperator?.removeItem(viewData)
                            remove(viewData)
                        }else{
                            Log.d(TAG, "どれにも当てはまらない")
                        }

                    }


                }else{

                }
            }

        }catch( e: Exception){
            Log.d(TAG, "onReceivedMessageでエラー発生", e)
        }
    }

    private val w = object : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "onOpenコネクション開始")

            while( couldNoteBeSentDataQueue.isNotEmpty() ){
                val item = couldNoteBeSentDataQueue.removeFirst()
                add(item)
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {

            Log.d(TAG, "onMessage: $text")

            if( text.isNotBlank() ){
                onReceivedMessage(text)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {

            Log.d(TAG, "onMessage, bytes $bytes")

        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "onClose: 通信が途絶えてしまった code: $code")
            mWebSocket = null

        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "onClosing: 通信を閉じている code: $code")

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(TAG, "onFailure: ERROR通信が途絶えてしまった", t)

            synchronized(captureNoteMap){
                val iterator = captureNoteMap.iterator()
                while( iterator.hasNext() ){
                    val next = iterator.next()
                    couldNoteBeSentDataQueue.add(next.value)
                    iterator.remove()
                }
            }
            mWebSocket = null

        }
    }
    /*private val wsListener = object : WebSocketListener{

        override fun onOpen(webSocket: WebSocket?, response: Response?) {

            mWebSocket = webSocket
            Log.d(TAG, "onOpenコネクション開始")
        }

        override fun onMessage(message: ResponseBody?) {
            val msg = message?.string()

        }

        override fun onClose(code: Int, reason: String?) {
        }

        override fun onFailure(e: IOException?, response: Response?) {
        }

        override fun onPong(payload: Buffer?) {
            //制御用　メッセージ
        }
    }*/
}
