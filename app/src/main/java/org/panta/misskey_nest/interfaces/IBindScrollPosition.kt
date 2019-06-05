package org.panta.misskey_nest.interfaces

import org.panta.misskey_nest.view_data.NoteViewData

//主にスクロールポジションをバインドするViewに実装しスクロールの位置を返す
interface IBindScrollPosition {
    fun bindFirstVisibleItemPosition(): Int?
    fun bindTotalItemCount(): Int?
    fun bindFindItemCount(): Int?
    fun pickViewData(index: Int): NoteViewData?
    fun pickViewData(viewData: NoteViewData): NoteViewData?
}