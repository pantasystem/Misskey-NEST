package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.usecase.interactor.NoteFormatUseCase
import org.panta.misskeynest.viewdata.NoteViewData
import java.util.*

class NoteFilter : IItemFilter<Note, NoteViewData> {
    override fun filter(items: List<Note>): List<NoteViewData> {
        /*val replyList = ArrayList<NoteViewData>()
        for(n in items){
            val noteType = NoteFormatUseCase().checkUpNoteType(n)
            //val reply = n.reply
            /*when(noteType){
                NoteFormatUseCase.NoteType.NOTE, NoteFormatUseCase.NoteType.QUOTE_RE_NOTE -> replyList.add(NoteViewData(n.id,false,n, type = noteType, reactionCountPairList = NoteFormatUseCase().createReactionCountPair(n.reactionCounts), toShowNote = n, updatedAt = date))
                NoteFormatUseCase.NoteType.RE_NOTE -> replyList.add(NoteViewData(n.id, false, n, type = noteType, reactionCountPairList = NoteFormatUseCase().createReactionCountPair(n.renote?.reactionCounts), toShowNote = n.renote!!, updatedAt = date))

                NoteFormatUseCase.NoteType.REPLY ->{

                    replyList.add(NoteViewData(n.id,false, n, type = NoteFormatUseCase.NoteType.REPLY, reactionCountPairList = NoteFormatUseCase().createReactionCountPair(n.reactionCounts), toShowNote = n, updatedAt = date))
                }
                else-> {
                    Log.w("AbsTimeline", "わからないタイプのノートが来てしまった:$n")
                }

            }*/
            replyList.addfilter(n)
        }
        return replyList*/
        return items.map{
            filter(it)
        }

    }

    override fun filter(item: Note): NoteViewData {
        val noteType = NoteFormatUseCase().checkUpNoteType(item)
        val date = Date()


        return when(noteType){
            NoteFormatUseCase.NoteType.NOTE, NoteFormatUseCase.NoteType.QUOTE_RE_NOTE ->{
                NoteViewData(item.id,false, item, type = noteType,
                    reactionCountPairList = NoteFormatUseCase().createReactionCountPair(item.reactionCounts), toShowNote = item, updatedAt = date)
            }
            NoteFormatUseCase.NoteType.RE_NOTE ->{
                NoteViewData(item.id, false, item, type = noteType,
                    reactionCountPairList = NoteFormatUseCase().createReactionCountPair(item.renote?.reactionCounts), toShowNote = item.renote!!, updatedAt = date)
            }
            NoteFormatUseCase.NoteType.REPLY ->{

                NoteViewData(item.id,false, item, type = NoteFormatUseCase.NoteType.REPLY,
                    reactionCountPairList = NoteFormatUseCase().createReactionCountPair(item.reactionCounts), toShowNote = item, updatedAt = date)
            }
            else -> throw IllegalArgumentException("許可されていない値です")

        }

    }
}