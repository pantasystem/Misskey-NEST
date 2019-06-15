package org.panta.misskeynest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.NotificationProperty
import org.panta.misskeynest.entity.RequestNotificationProperty
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.usecase.NoteAdjustment
import org.panta.misskeynest.viewdata.NoteViewData
import org.panta.misskeynest.viewdata.NotificationViewData
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class NotificationRepository(private val domain: String, private val authKey: String): IItemRepository<NotificationViewData>{

    private val connection = OkHttpConnection()
    private val mapper = jacksonObjectMapper()
    private val noteAd = NoteAdjustment()


    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<NotificationViewData>?) -> Unit)= GlobalScope.launch {
        try{
            val reqObj = RequestNotificationProperty(i = authKey, sinceId = sinceId ,limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)

            val data = getNotificationData(reqJson)
            if(data == null){
                callBack(null)
            }else{
                callBack(reverseList(data))
            }
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
        }

    }
    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<NotificationViewData>?) -> Unit) = GlobalScope.launch {
        try{
            val reqObj = RequestNotificationProperty(i = authKey, untilId = untilId ,limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)
            callBack(getNotificationData(reqJson))
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
        }
    }
    override fun getItems(callBack: (timeline: List<NotificationViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val reqObj = RequestNotificationProperty(i = authKey, limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)
            callBack(getNotificationData(reqJson))

        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
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

    private suspend fun getNotificationData(json: String): List<NotificationViewData>?{
        val resJson = connection.postString(URL("$domain/api/i/notifications"), json)
        return if(resJson == null){
            null
        }else{
            val resObj: List<NotificationProperty> = mapper.readValue(resJson)
            makeViewData(resObj)
        }

    }

    private fun makeViewData(list: List<NotificationProperty>): List<NotificationViewData>{
        return list.map{
            if(it.note == null){
                NotificationViewData(it.id,false, it, null)
            }else{
                val viewData = NoteViewData(it.note.id, false,it.note, it.note,noteAd.checkUpNoteType(it.note), noteAd.createReactionCountPair(it.note.reactionCounts), Date())
                NotificationViewData(it.id, false, it, viewData)
            }
        }
    }

    private fun reverseList(list: List<NotificationViewData>): List<NotificationViewData>{
        val arrayList = ArrayList<NotificationViewData>()
        for(i in (list.size - 1).downTo(0)){
            arrayList.add(list[i])
        }
        return arrayList
    }


}