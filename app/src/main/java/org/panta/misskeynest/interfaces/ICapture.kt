package org.panta.misskeynest.interfaces

import org.panta.misskeynest.viewdata.NoteViewData

interface ICapture{
    fun captureNote(noteViewData: NoteViewData)
    fun cancelCaptureNote(noteViewData: NoteViewData): Boolean
}