package org.panta.misskeynest.filter

import android.util.Log
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.usecase.NoteAdjustment
import org.panta.misskeynest.viewdata.NoteViewData
import java.util.*

class NoteFilter : IItemFilter<Note, NoteViewData> {
    override fun filter(items: List<Note>): List<NoteViewData> {
        val replyList = ArrayList<NoteViewData>()
        for(n in items){
            val noteType = NoteAdjustment().checkUpNoteType(n)
            val reply = n.reply
            val date = Date()
            when(noteType){
                NoteAdjustment.NoteType.NOTE, NoteAdjustment.NoteType.QUOTE_RE_NOTE -> replyList.add(NoteViewData(n.id,false,n, type = noteType, reactionCountPairList = NoteAdjustment().createReactionCountPair(n.reactionCounts), toShowNote = n, updatedAt = date))
                NoteAdjustment.NoteType.RE_NOTE -> replyList.add(NoteViewData(n.id, false, n, type = noteType, reactionCountPairList = NoteAdjustment().createReactionCountPair(n.renote?.reactionCounts), toShowNote = n.renote!!, updatedAt = date))

                NoteAdjustment.NoteType.REPLY ->{

                    replyList.add(NoteViewData(n.id,false, n, type = NoteAdjustment.NoteType.REPLY, reactionCountPairList = NoteAdjustment().createReactionCountPair(n.reactionCounts), toShowNote = n, updatedAt = date))
                }
                else-> {
                    Log.w("AbsTimeline", "わからないタイプのノートが来てしまった:$n")
                }
            }

        }
        return replyList

    }
}