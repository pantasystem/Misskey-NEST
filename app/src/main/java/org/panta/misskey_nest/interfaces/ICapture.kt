package org.panta.misskey_nest.interfaces

import org.panta.misskey_nest.entity.Note
import org.panta.misskey_nest.view_data.NoteViewData

interface ICapture{
    fun captureNote(noteViewData: NoteViewData)
    fun cancelCaptureNote(noteViewData: NoteViewData): Boolean
}