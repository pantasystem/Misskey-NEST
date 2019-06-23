package org.panta.misskeynest.repository.remote

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.INoteRepository
import java.net.URL

class NoteRepository(private val mConnectionProperty: ConnectionProperty): INoteRepository {
    private val i = mConnectionProperty.i
    private val childEndpoint = URL("${mConnectionProperty.domain}/api/notes/children")
    private val conversationEndpoint = URL("${mConnectionProperty.domain}/api/notes/conversation")

    private val mConnection = OkHttpConnection()

    override fun send(property: CreateNoteProperty): Boolean{
        return try{
            val json = jacksonObjectMapper().writeValueAsString(property)
            Log.d("NoteRepository" , "JSON $json")
            val response = mConnection.postString(URL("${mConnectionProperty.domain}/api/notes/create"), json)
            response != null
        }catch(e: Exception){
            Log.w("NoteRepository", "送信中に問題発生", e)
            false
        }
    }


    override fun getNote(noteId: String): Note?{
        val map = HashMap<String, String>()
        map["i"] = i
        map["noteId"] = noteId
        val json = jacksonObjectMapper().writeValueAsString(map)

        try{
            val response = mConnection.postString(URL("${mConnectionProperty.domain}/api/notes/show"), json)
                ?:return null

            return jacksonObjectMapper().readValue(response)
            //val viewData = NoteFormatUseCase().createViewData(responseData)

        }catch(e: Exception){
            Log.w("NoteRepository","getNote", e)
            return null
        }

    }

    override fun remove(note: Note): Boolean{
        val map = hashMapOf("i" to mConnectionProperty.i)
        map["noteId"] = note.id
        val json = jacksonObjectMapper().writeValueAsString(map)
        val response = mConnection.postString(URL("${mConnectionProperty.domain}/api/notes/delete"), json)
        return response != null
    }

    override fun getChild(noteId: String): List<Note>?{
        val valueMap = mapOf("i" to i, "noteId" to noteId, "limit" to 30)
        val json = jacksonObjectMapper().writeValueAsString(valueMap)
        val response = mConnection.postString(childEndpoint, json)?: return null
        return jacksonObjectMapper().readValue(response)
    }

    override fun getConversation(noteId: String): List<Note>?{
        val valueMap = mapOf("i" to i, "noteId" to noteId, "limit" to 30)
        val json = jacksonObjectMapper().writeValueAsString(valueMap)
        val response = mConnection.postString(conversationEndpoint, json)?: return null
        return jacksonObjectMapper().readValue(response)
    }
}