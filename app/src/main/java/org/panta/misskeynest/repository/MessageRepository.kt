package org.panta.misskeynest.repository

import kotlinx.coroutines.Job
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.view_data.NoteViewData

class MessageRepository : IItemRepository<NoteViewData>{
    fun create(){

    }

    override fun getItems(callBack: (timeline: List<NoteViewData>?) -> Unit): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<NoteViewData>?) -> Unit): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<NoteViewData>?) -> Unit): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}