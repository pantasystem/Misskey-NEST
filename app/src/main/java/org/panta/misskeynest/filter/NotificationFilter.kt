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
            filter(it)
        }
    }

    override fun filter(item: NotificationProperty): NotificationViewData {
        return if(item.note == null){
            NotificationViewData(item.id,false, item, null)
        }else{
            val viewData = NoteViewData(item.note.id, false,item.note, item.note,noteAd.checkUpNoteType(item.note), noteAd.createReactionCountPair(item.note.reactionCounts), Date())
            NotificationViewData(item.id, false, item, viewData)
        }
    }
}