package org.panta.misskeynest.repository.remote

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.NotificationProperty
import org.panta.misskeynest.entity.RequestNotificationProperty
import org.panta.misskeynest.interactor.NoteFormatUseCase
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.network.OkHttpConnection
import java.net.URL

class NotificationRepository(private val domain: String, private val authKey: String): IItemRepository<NotificationProperty>{

    private val connection = OkHttpConnection()
    private val mapper = jacksonObjectMapper()
    private val noteAd = NoteFormatUseCase()


    override fun getItemsUseSinceId(sinceId: String): List<NotificationProperty>?{
        return try{
            val reqObj = RequestNotificationProperty(i = authKey, sinceId = sinceId ,limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)

            val data = getNotificationData(reqJson)
            if(data == null){
                null
            }else{
                reverseList(data)
            }
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
            null
        }
    }
    override fun getItemsUseUntilId(untilId: String): List<NotificationProperty>?{
        return try{
            val reqObj = RequestNotificationProperty(i = authKey, untilId = untilId ,limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)
            getNotificationData(reqJson)
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
            null
        }
    }
    override fun getItems(): List<NotificationProperty>?{
        return try{
            val reqObj = RequestNotificationProperty(i = authKey, limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)
            getNotificationData(reqJson)
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
            null
        }
    }

    fun markAllAsRead() = GlobalScope.launch{
        try{
            val map = mapOf("i" to authKey)
            connection.postString(URL("$domain/api/notifications/mark-all-as-read"), mapper.writeValueAsString(map))
        }catch(e: Exception){
            Log.w("Notification", "markAllAsReadでエラー発生", e)
        }
    }

    private fun getNotificationData(json: String): List<NotificationProperty>?{
        val resJson = connection.postString(URL("$domain/api/i/notifications"), json)
        return if(resJson == null){
            null
        }else{
            return mapper.readValue(resJson)
            //makeViewData(resObj)
        }

    }



    private fun reverseList(list: List<NotificationProperty>): List<NotificationProperty>{
        val arrayList = ArrayList<NotificationProperty>()
        for(i in (list.size - 1).downTo(0)){
            arrayList.add(list[i])
        }
        return arrayList
    }


}