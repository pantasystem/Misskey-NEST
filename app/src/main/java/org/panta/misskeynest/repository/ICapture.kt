package org.panta.misskeynest.repository

import org.panta.misskeynest.viewdata.NoteViewData

interface ICapture{
    fun captureNote(noteViewData: NoteViewData)
    fun unCaptureNote(viewData: NoteViewData, isRemove: Boolean = true)
}