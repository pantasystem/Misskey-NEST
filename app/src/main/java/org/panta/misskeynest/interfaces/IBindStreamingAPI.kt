package org.panta.misskeynest.interfaces

import org.panta.misskeynest.viewdata.NoteViewData

//TimelineとStreamingAPIを橋渡しするPresenter、Viewに実装する。
@Deprecated("あまり意味のないインターフェース") interface IBindStreamingAPI {
    var bindScrollPosition: IBindScrollPosition

    fun onUpdateNote(data: NoteViewData)
    fun onRemoveNote(data: NoteViewData)
    fun addFirstNote(data: NoteViewData)
}