package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.NotificationProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.usecase.interactor.NoteFormatUseCase
import org.panta.misskeynest.viewdata.NoteViewData
import org.panta.misskeynest.viewdata.NotificationViewData
import java.util.*

class NotificationFilter : IItemFilter<NotificationProperty, NotificationViewData>{
    private val noteAd = NoteFormatUseCase()
    override fun filter(items: List<NotificationProperty>): List<NotificationViewData> {
        return items.map{
            if(it.note == null){
                NotificationViewData(it.id,false, it, null)
            }else{
                val viewData = NoteViewData(it.note.id, false,it.note, it.note,noteAd.checkUpNoteType(it.note), noteAd.createReactionCountPair(it.note.reactionCounts), Date())
                NotificationViewData(it.id, false, it, viewData)
            }
        }
    }
}