package org.panta.misskeynest.interfaces

//主にスクロールポジションをバインドするViewに実装しスクロールの位置を返す
interface IBindScrollPosition {
    fun bindFirstVisibleItemPosition(): Int?
    fun bindTotalItemCount(): Int?
    fun bindFindItemCount(): Int?
    //@Deprecated("意味のないメソッド") fun pickViewData(index: Int): NoteViewData?
    //@Deprecated("意味のないメソッド") fun pickViewData(viewData: NoteViewData): NoteViewData?
}