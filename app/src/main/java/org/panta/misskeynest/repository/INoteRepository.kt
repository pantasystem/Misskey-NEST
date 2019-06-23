package org.panta.misskeynest.repository

import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.entity.Note

interface INoteRepository {
    fun send(property: CreateNoteProperty): Boolean
    fun getNote(noteId: String): Note?
    fun remove(note: Note): Boolean
    fun getChild(noteId: String): List<Note>?

    fun getConversation(noteId: String): List<Note>?


}