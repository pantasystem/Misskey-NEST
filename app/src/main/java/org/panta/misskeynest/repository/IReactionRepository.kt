package org.panta.misskeynest.repository

interface IReactionRepository {
    fun sendReaction(noteId: String, type: String): Boolean
    fun deleteReaction(noteId: String): Boolean
}