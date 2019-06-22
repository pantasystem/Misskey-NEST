package org.panta.misskeynest.viewdata

const val PIN_NOTE = 0
const val RECENT_NOTE = 1
const val IMAGE_NOTE = 2

data class OverviewViewData(
    val type: Int,
    val item: NoteViewData?,
    val itemList: List<NoteViewData>?
)