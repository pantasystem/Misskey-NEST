package org.panta.misskeynest.usecase.interactor

import android.util.Log
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.ReactionCountPair
import org.panta.misskeynest.viewdata.NoteViewData
import java.util.*

class NoteFormatUseCase(private val isDeployReplyTo: Boolean = true){
    enum class NoteType{
        REPLY,
        REPLY_TO,   //返信先
        NOTE,
        RE_NOTE,
        QUOTE_RE_NOTE,
        DO_NOT_KNOW
    }
    fun insertReplyAndCreateInfo(list: List<Note>): List<NoteViewData>{
        val replyList = ArrayList<NoteViewData>()
        for(n in list){
            val noteType = checkUpNoteType(n)
            val reply = n.reply
            val date = Date()
            when(noteType){
                NoteType.NOTE, NoteType.QUOTE_RE_NOTE -> replyList.add(NoteViewData(n.id,false,n, type = noteType, reactionCountPairList = createReactionCountPair(n.reactionCounts), toShowNote = n, updatedAt = date))
                NoteType.RE_NOTE -> replyList.add(NoteViewData(n.id, false, n, type = noteType, reactionCountPairList = createReactionCountPair(n.renote?.reactionCounts), toShowNote = n.renote!!, updatedAt = date))

                NoteType.REPLY ->{

                    /*if(isDeployReplyTo){
                        replyList.add(NoteViewData(reply!!.id, true,reply, type = NoteType.REPLY_TO, reactionCountPairList = createReactionCountPair(reply.reactionCounts)))
                    }*/
                    replyList.add(NoteViewData(n.id,false, n, type = NoteType.REPLY, reactionCountPairList = createReactionCountPair(n.reactionCounts), toShowNote = n, updatedAt = date))
                }
                else-> {
                    Log.w("AbsTimeline", "わからないタイプのノートが来てしまった:$n")
                }
            }

        }
        return replyList
    }

    fun createViewData(note: Note): NoteViewData?{
        val noteType = checkUpNoteType(note)
        val reply = note.reply
        val date = Date()
        return when(noteType){
            NoteType.NOTE, NoteType.QUOTE_RE_NOTE -> NoteViewData(note.id,false,note, type = noteType, reactionCountPairList = createReactionCountPair(note.reactionCounts), toShowNote = note, updatedAt = date)
            NoteType.RE_NOTE -> NoteViewData(note.id, false, note, type = noteType, reactionCountPairList = createReactionCountPair(note.renote?.reactionCounts), toShowNote = note.renote!!, updatedAt = date)

            NoteType.REPLY ->{

                /*if(isDeployReplyTo){
                    replyList.add(NoteViewData(reply!!.id, true,reply, type = NoteType.REPLY_TO, reactionCountPairList = createReactionCountPair(reply.reactionCounts)))
                }*/
                NoteViewData(note.id,false, note, type = NoteType.REPLY, reactionCountPairList = createReactionCountPair(note.reactionCounts), toShowNote = note, updatedAt = date)
            }
            else-> {
                Log.w("AbsTimeline", "わからないタイプのノートが来てしまった:$note")
                return null
            }
        }
    }



    //FIXME メディアOnlyの時にうまく認識できない
    fun checkUpNoteType(note: Note): NoteType {
        return if(note.reply != null){
            //これはリプ
            NoteType.REPLY
        }else if(note.reNoteId == null && (note.text != null || note.files != null)){
            //これはNote
            NoteType.NOTE
        }else if(note.reNoteId != null && note.text == null && note.files.isNullOrEmpty()){
            //これはリノート
            NoteType.RE_NOTE

        }else if(note.reNoteId != null && (note.text != null || note.files != null)){
            //これは引用リノート
            NoteType.QUOTE_RE_NOTE
        }else{
            NoteType.DO_NOT_KNOW
        }
    }

    fun createReactionCountPair(reactionCount: Map<String, Int>?): List<ReactionCountPair>{
        if(reactionCount == null){
            return emptyList()
        }
        return ReactionCountPair.createList(reactionCount)
    }
}