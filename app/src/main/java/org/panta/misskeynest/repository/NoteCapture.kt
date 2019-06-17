package org.panta.misskeynest.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.panta.misskeynest.entity.BodyProperty
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.StreamingProperty
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.usecase.NoteUpdater
import org.panta.misskeynest.viewdata.NoteViewData
import java.net.URI

class NoteCapture(private val connectionInfo: ConnectionProperty,  private val mAdapterOperator: IOperationAdapter<NoteViewData>){


    private var socket = Socket()
    init{
        socket.connect()
    }
    val tag = "StreamingChannel"

    //TODO キャッシュとViewのデータが別のデータのため同期がとれていないので取れるようにする
    private var captureViewData = ArrayList<NoteViewData>()
    private val noteUpdater = NoteUpdater()

    fun clearCapture(){
        synchronized(captureViewData){
            captureViewData.forEach{
                unCaptureNote(it,false)
            }
            captureViewData = ArrayList()
        }
    }
    
    fun captureNote(viewData: NoteViewData){
        if(socket.isClosed){
            socket = Socket()
            socket.connect()
        }
        val data = StreamingProperty(type = "subNote",
            body = BodyProperty(id = viewData.toShowNote.id)
        )
        if(socket.isOpen){
            Log.d(tag, "送信先 $\"${connectionInfo.domain.replace("https://", "wss://")}/streaming?${connectionInfo.i}\"")
            socket.send(jacksonObjectMapper().writeValueAsString(data))
            captureViewData.add(viewData)
        }
    }
    
    fun unCaptureNote(viewData: NoteViewData, isRemove: Boolean = true){
        val data = StreamingProperty(type = "unsubNote",
            body = BodyProperty(id = viewData.toShowNote.id)
        )
        if(socket.isOpen){
            socket.send(jacksonObjectMapper().writeValueAsString(data))

        }
        if(isRemove){
            captureViewData.remove(viewData)
        }
    }

    inner class Socket : WebSocketClient(URI("${connectionInfo.domain.replace("https://", "wss://")}/streaming?${connectionInfo.i}")){
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.d(tag, "onOpen")
        }

        override fun onMessage(message: String?) {
            Log.d(tag, "onMessage $message")
            message?: return
            try{
                val obj = jacksonObjectMapper().readValue<StreamingProperty>(message)
                if(obj.type == "noteUpdated"){

                    val id = obj.body.id
                    val userId = obj.body.body?.userId!!
                    val isMyReaction = connectionInfo.userPrimaryId == userId
                    val reaction = obj.body.body.reaction!!

                    //TODO キャッシュとViewのデータが別のデータのため同期がとれていないので取れるようにする


                    captureViewData.filter{
                        it.toShowNote.id == id
                    }.forEach {
                        val viewsData = mAdapterOperator.getItem(it)

                        if(viewsData != null){
                            val updatedViewData = if(obj.body.type == "reacted"){
                                noteUpdater.addReaction(reaction, viewsData, isMyReaction)
                            }else{
                                noteUpdater.removeReaction(reaction, viewsData, isMyReaction)
                            }
                            //bindStreamingProperty.onUpdateNote(updatedViewData)
                            Handler(Looper.getMainLooper()).post{
                                mAdapterOperator.updateItem(updatedViewData)
                            }
                        }
                    }

                }


            }catch(e: Exception){

            }
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d(tag, "close code:$code")
        }

        override fun onError(ex: Exception?) {
            Log.d(tag, "error", ex)
        }
    }




}