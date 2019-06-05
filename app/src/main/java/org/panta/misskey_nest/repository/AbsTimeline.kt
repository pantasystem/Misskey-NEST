package org.panta.misskey_nest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.entity.Note
import org.panta.misskey_nest.entity.ReactionCountPair
import org.panta.misskey_nest.interfaces.IItemRepository
import org.panta.misskey_nest.network.HttpsConnection
import org.panta.misskey_nest.network.OkHttpConnection
import org.panta.misskey_nest.network.StreamConverter
import org.panta.misskey_nest.usecase.NoteAdjustment
import org.panta.misskey_nest.view_data.NoteViewData
import java.net.URL
import java.util.*

abstract class AbsTimeline(private val timelineURL: URL, private val isDeployReplyTo: Boolean = false): IItemRepository<NoteViewData>{


    private val mConnection = OkHttpConnection()
    private val mapper = jacksonObjectMapper()
    private val converter = StreamConverter()

    private val noteAd = NoteAdjustment(isDeployReplyTo)


    //FIXME 直接Dataクラスを送信するようにする
    abstract fun createRequestTimelineJson(sinceId: String? = null, untilId: String? = null, sinceDate: Long? = null, untilDate: Long? = null): String

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<NoteViewData>?)->Unit) = GlobalScope.launch{
        try{
            val jsonToRequest = createRequestTimelineJson(sinceId = sinceId)
            val noteList = requestTimeline(jsonToRequest)
            if(noteList == null){
                callBack(null)
            }else{
                val list:List<Note> = reverseTimeline(noteList)
                callBack(noteAd.insertReplyAndCreateInfo(list))
            }

        }catch(e: Exception){
            e.printStackTrace()

        }


    }

    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<NoteViewData>?)->Unit) = GlobalScope.launch{
        try{
            val jsonToRequest = createRequestTimelineJson(untilId = untilId)

            val list:List<Note>? = requestTimeline(jsonToRequest)
            if(list == null){
                callBack(null)
            }else{
                callBack(noteAd.insertReplyAndCreateInfo(list))
            }
        }catch(e: Exception){
            e.printStackTrace()

        }


    }


    override fun getItems(callBack: (timeline: List<NoteViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val cacheTimeline = emptyList<Note>()
            val timeline = if(cacheTimeline.isEmpty()){
                requestTimeline(createRequestTimelineJson(untilDate = Date().time))
            }else{
                cacheTimeline
            }

            if(timeline == null){
                callBack(null)
            }else{
                callBack(noteAd.insertReplyAndCreateInfo(timeline))
            }
        }catch(e: Exception){
            e.printStackTrace()
        }

    }



    private fun requestTimeline(json: String): List<Note>?{
        Log.d("AbsTimeline", "json $json")
        val receivedResult =mConnection.postString(timelineURL, json)
        return if(receivedResult == null){
            null
        }else{
            mapper.readValue(receivedResult)
        }
    }

    //FIXME　他のクラスでもよく使用するので分離予定
    private fun reverseTimeline(list: List<Note>):List<Note>{
        val reversedList = ArrayList<Note>()
        for(n in list.size - 1 downTo 0){
            reversedList.add(list[n])
        }
        return reversedList
    }







    private fun createReactionCountPair(reactionCount: Map<String, Int>?): List<ReactionCountPair>{
        if(reactionCount == null){
            return emptyList()
        }
        return ReactionCountPair.createList(reactionCount)
    }


}