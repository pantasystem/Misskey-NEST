package org.panta.misskeynest.interfaces

import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.view_data.NoteViewData

interface ICapture{
    fun captureNote(noteViewData: NoteViewData)
    fun cancelCaptureNote(noteViewData: NoteViewData): Boolean
}