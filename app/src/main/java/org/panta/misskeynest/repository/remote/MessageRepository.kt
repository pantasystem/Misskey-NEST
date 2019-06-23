package org.panta.misskeynest.repository.remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IMessageRepository
import java.net.URL

class MessageRepository(private val connectionProperty: ConnectionProperty): IMessageRepository{

    private val httpConnection = OkHttpConnection()

    override fun getHistory(isGroup: Boolean): List<MessageProperty>?{
        val json = jacksonObjectMapper().writeValueAsString(mapOf("i" to connectionProperty.i, "limit" to 100, "group" to isGroup))
        val response =  httpConnection.postString(URL("${connectionProperty.domain}/api/messaging/history"), json)
        response?: return null

        return jacksonObjectMapper().readValue(response)
    }

    override fun create(userId: String?, groupId: String?, text: String, fileId: String?): Boolean{

        //どちらかしか許容しない
        if( ( userId == null ) xor ( groupId == null ) ){
            val objectMap = HashMap<String, Any>()
            objectMap["i"] = connectionProperty.i
            if( userId != null ) objectMap["userId"] = userId
            if( groupId != null ) objectMap["groupId"] = groupId
            if( fileId != null ) objectMap["fileId"] = fileId
            objectMap["text"] = text
            val json = jacksonObjectMapper().writeValueAsString(objectMap)

            return httpConnection.postString(URL(connectionProperty.domain), json) != null
        }else{
            throw IllegalArgumentException("userId, groupIdそのどちらかしか許容されていません")
        }
    }

    override fun getMessages(userId: String?, groupId: String?, untilId: String?, sinceId: String?): List<MessageProperty>?{
        //どちらかしか許容しない
        if( ( userId == null ) xor ( groupId == null ) ){
            val objectMap = HashMap<String, Any>()
            objectMap["i"] = connectionProperty.i
            if( userId != null ) objectMap["userId"] = userId
            if( groupId != null ) objectMap["groupId"] = groupId

            if( !(untilId != null  && sinceId != null ) ){
                if( untilId != null ) objectMap["untilId"] = untilId
                if( sinceId != null ) objectMap["sinceId"] = sinceId
            }else{
                throw IllegalArgumentException("untilId, sinceIdそのどちらかしか許容されていません")
            }
            val json = jacksonObjectMapper().writeValueAsString(objectMap)

            val response = httpConnection.postString(URL("${connectionProperty.domain}/api/messaging/messages"), json)
            response?: return null

            return jacksonObjectMapper().readValue(response)
        }else{
            throw IllegalArgumentException("userId, groupIdそのどちらかしか許容されていません")
        }
    }



}