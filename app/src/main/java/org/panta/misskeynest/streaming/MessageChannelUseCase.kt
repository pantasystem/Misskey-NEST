package org.panta.misskeynest.streaming

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.*
import org.panta.misskeynest.entity.BodyProperty
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.entity.StreamingProperty
import org.panta.misskeynest.interfaces.CallBackListener
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.MessageDataType
import org.panta.misskeynest.viewdata.MessageViewData
import java.util.*

private const val TAG = "MessageChannelUseCase"
class MessageChannelUseCase(private val mConnectionProperty: ConnectionProperty, private val mFilter: IItemFilter<MessageProperty, MessageViewData>, val messagingProperty: MessageViewData) :
    IMessageChannelUseCase {

    override var messageReceivedListener: CallBackListener<List<MessageViewData>>? = null

    private val mClient = OkHttpClient()
    private var mWebSocket: WebSocket? = null

    private var mChannelId: Int = 0
    private var mMessagingChannelId: Int = 0

    override fun start() {
        val wssDomain = mConnectionProperty.domain.replace("https://", "wss://")

        val request = Request.Builder()
            .url("$wssDomain/streaming?i=${mConnectionProperty.i}")
            .build()
        mWebSocket = mClient.newWebSocket(request, webSocketListener)

        mChannelId = Random().nextInt(10000000)
        mMessagingChannelId = Random().nextInt(10000000)


    }


    private val webSocketListener = object : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "onOpen")



            val connectObj = StreamingProperty<Any>( type = "connect",
                body = BodyProperty(
                    channel = "main",
                    id = mChannelId.toString()
                )

            )
            val json = jacksonObjectMapper().writeValueAsString(connectObj)
            Log.d(TAG, json)
            webSocket.send(json)

            val messagingParams = when( messagingProperty.messageType ){
                MessageDataType.MESSAGE_GROUP, MessageDataType.HISTORY_GROUP ->{
                    val id = messagingProperty.message.group?.id
                    mapOf("group" to id)
                }
                MessageDataType.MESSAGE_USER, MessageDataType.HISTORY_USER ->{
                    val userId = messagingProperty.message.recipient?.id
                    if( userId == null ){
                        null
                    } else {
                        mapOf("otherparty" to userId)
                    }
                }
            }

            /*val messagingObj = StreamingProperty<Any>(
                type = "connect",
                body = BodyProperty(
                    channel = "messaging",
                    id = mMessagingChannelId.toString(),
                    params = messagingParams
                )
            )
            val messagingJson = jacksonObjectMapper().writeValueAsString(messagingObj)
            Log.d(TAG, "messagingJson $messagingJson")
            webSocket.send(messagingJson)*/

        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(TAG, "onFailure")
            Thread.sleep(1000)
            start()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(TAG, "受信した内容 :$text")

            if(text.isBlank()) return

            try{
                val obj: StreamingProperty<MessageProperty> = jacksonObjectMapper().readValue(text)
                val body = obj.body.body
                if( body != null){
                    Log.d(TAG,"parse成功 $body")
                    val viewData= mFilter.filter(obj.body.body)
                    messageReceivedListener?.callBack(listOf(viewData))
                }
            }catch(e: Exception){
                Log.d(TAG, "onMessageでエラー発生", e)
            }


           // StreamingProperty<MessageProperty>()
        }
    }


}