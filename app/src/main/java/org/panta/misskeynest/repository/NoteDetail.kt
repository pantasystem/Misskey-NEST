package org.panta.misskeynest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.ReactionCountPair
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.usecase.NoteAdjustment
import org.panta.misskeynest.view_data.NoteViewData
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

//FIXME そもそもDESCRIPTIONは別のActivity、別のFragmentで定義する可能性があるのでITimelineという型に
// こだわる必要性がない可能性
//TimelineAdapterのみ再利用する
class NoteDetail(private val connectionProperty: ConnectionProperty){

    //過去ほど上になり現在に近いほど下になる通常のタイムラインとは真逆

    private val i = connectionProperty.i
    private val childEndpoint = URL("${connectionProperty.domain}/api/notes/children")
    private val conversationEndpoint = URL("${connectionProperty.domain}/api/notes/conversation")

    private val mConnection = OkHttpConnection()

    fun getChild(noteId: String): List<Note>?{
        val valueMap = mapOf("i" to i, "noteId" to noteId, "limit" to 30)
        val json = jacksonObjectMapper().writeValueAsString(valueMap)
        val response = mConnection.postString(childEndpoint, json)?: return null
        return jacksonObjectMapper().readValue(response)
    }

    fun getConversation(noteId: String): List<Note>?{
        val valueMap = mapOf("i" to i, "noteId" to noteId, "limit" to 30)
        val json = jacksonObjectMapper().writeValueAsString(valueMap)
        val response = mConnection.postString(conversationEndpoint, json)?: return null
        return jacksonObjectMapper().readValue(response)
    }

    //FIXME 色々いい加減なので修正する
    fun getOriginNotes(note: Note, callBack: (timeline: List<NoteViewData>?) -> Unit) = GlobalScope.launch{
        try{
            var reply: Note? = note.reply
            val replyList = ArrayList<NoteViewData>()
            replyList.add(NoteViewData(id = note.id, isIgnore = false ,note = note, type = NoteAdjustment.NoteType.NOTE, reactionCountPairList = ReactionCountPair.createList(note.reactionCounts!!), toShowNote = note, updatedAt = Date()))
            while(reply != null){
                val reactionPair = if(reply.reactionCounts == null){
                    emptyList()
                }else{
                    //ReactionCountPair.createList(reply.reactionCounts!!)
                    reply.reactionCounts!!.map{
                        ReactionCountPair(it.key, it.value)
                    }
                }
                replyList.add(NoteViewData(id = reply.id, isIgnore = false ,note = reply, type = NoteAdjustment.NoteType.NOTE, reactionCountPairList = reactionPair, toShowNote = reply, updatedAt =Date()))
                reply = reply.reply
            }
            callBack(reverseTimeline(replyList))
        }catch(e: Exception){
            Log.w("Description", "getOriginNotesでエラー発生", e)
        }

    }

    private fun reverseTimeline(list: List<NoteViewData>):List<NoteViewData>{
        val reversedList = java.util.ArrayList<NoteViewData>()
        for(n in list.size - 1 downTo 0){
            reversedList.add(list[n])
        }
        return reversedList
    }

}