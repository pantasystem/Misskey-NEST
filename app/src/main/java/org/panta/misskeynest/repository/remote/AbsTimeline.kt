package org.panta.misskeynest.repository.remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.ReactionCountPair
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.usecase.interactor.NoteFormatUseCase
import java.net.URL
import java.util.*

abstract class AbsTimeline(private val timelineURL: URL, private val isDeployReplyTo: Boolean = false):
    IItemRepository<Note> {


    private val mConnection = OkHttpConnection()
    private val mapper = jacksonObjectMapper()

    private val noteAd = NoteFormatUseCase(isDeployReplyTo)


    //FIXME 直接Dataクラスを送信するようにする
    abstract fun createRequestTimelineJson(sinceId: String? = null, untilId: String? = null, sinceDate: Long? = null, untilDate: Long? = null): String

    override fun getItemsUseSinceId(sinceId: String): List<Note>?{
        return try{
            val jsonToRequest = createRequestTimelineJson(sinceId = sinceId)
            val noteList = requestTimeline(jsonToRequest)
            if(noteList == null){
                null
            }else{
                return noteList.reversed()//reverseTimeline(noteList)
            }

        }catch(e: Exception){
            e.printStackTrace()
            null
        }
    }

    override fun getItemsUseUntilId(untilId: String): List<Note>?{
        return try{
            val jsonToRequest = createRequestTimelineJson(untilId = untilId)

            requestTimeline(jsonToRequest)
            /*if(list == null){
                null
            }else{
                noteAd.insertReplyAndCreateInfo(list)
            }*/
        }catch(e: Exception){
            e.printStackTrace()

            null
        }
    }


    override fun getItems(): List<Note>?{
        try{
            val cacheTimeline = emptyList<Note>()
            val timeline = if(cacheTimeline.isEmpty()){
                requestTimeline(createRequestTimelineJson(untilDate = Date().time))
            }else{
                cacheTimeline
            }

            /*return if(timeline == null){
                null
            }else{
                noteAd.insertReplyAndCreateInfo(timeline)
            }*/
            return timeline
        }catch(e: Exception){
            e.printStackTrace()
            return null
        }
    }


    private fun requestTimeline(json: String): List<Note>?{
        //Log.d("AbsTimeline", "json $json")
        val receivedResult =mConnection.postString(timelineURL, json)
        return if(receivedResult == null){
            null
        }else{
            mapper.readValue(receivedResult)
        }
    }

    /*private fun reverseTimeline(list: List<Note>):List<Note>{
        val reversedList = ArrayList<Note>()
        for(n in list.size - 1 downTo 0){
            reversedList.add(list[n])
        }
        return reversedList
    }*/



    private fun createReactionCountPair(reactionCount: Map<String, Int>?): List<ReactionCountPair>{
        if(reactionCount == null){
            return emptyList()
        }
        return ReactionCountPair.createList(reactionCount)
    }


}