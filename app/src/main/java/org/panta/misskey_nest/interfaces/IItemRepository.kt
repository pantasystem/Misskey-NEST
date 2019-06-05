package org.panta.misskey_nest.interfaces

import kotlinx.coroutines.Job
import org.panta.misskey_nest.entity.Note
import org.panta.misskey_nest.view_data.NoteViewData

interface IItemRepository<E: ID> {
    fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<E>?)->Unit):Job

    fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<E>?)->Unit):Job

    fun getItems(callBack: (timeline: List<E>?) -> Unit):Job


}