package org.panta.misskey_nest.interfaces

import org.panta.misskey_nest.view_data.NoteViewData

//TimelineとStreamingAPIを橋渡しするPresenter、Viewに実装する。
interface IBindStreamingAPI {
    var bindScrollPosition: IBindScrollPosition

    fun onUpdateNote(data: NoteViewData)
    fun onRemoveNote(data: NoteViewData)
    fun addFirstNote(data: NoteViewData)
}