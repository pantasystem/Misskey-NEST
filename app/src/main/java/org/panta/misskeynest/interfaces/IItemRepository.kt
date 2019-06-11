package org.panta.misskeynest.interfaces

import kotlinx.coroutines.Job
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.view_data.NoteViewData

interface IItemRepository<E: ID> {
    fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<E>?)->Unit):Job

    fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<E>?)->Unit):Job

    fun getItems(callBack: (timeline: List<E>?) -> Unit):Job


}