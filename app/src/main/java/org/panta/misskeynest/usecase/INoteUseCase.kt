package org.panta.misskeynest.usecase

import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.viewdata.NoteViewData

interface INoteUseCase {
    fun send(property: CreateNoteProperty, callBack: (Boolean)->Unit)
    fun getNote(noteId: String, callBack: (NoteViewData?)-> Unit)
    fun remove(note: Note, callBack: (Boolean)-> Unit)
}