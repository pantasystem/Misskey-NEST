package org.panta.misskeynest.repository.remote

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.panta.misskeynest.entity.ReactionCreateXorDeleteProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IReactionRepository
import java.net.URL

class ReactionRepository(private val domain: String, private val authKey: String): IReactionRepository{

    private val connection =  OkHttpConnection()
    override fun sendReaction(noteId: String, type: String): Boolean{
        return try{
            val data = ReactionCreateXorDeleteProperty( i = authKey, noteId = noteId, reaction = type)
            val json = jacksonObjectMapper().writeValueAsString(data)
            val stream = connection.postString(URL("$domain/api/notes/reactions/create"), json)
            Log.d("","受け取った内容 $stream")
            stream != null
        }catch(e: Exception){
            Log.w("", "リアクション送信中にエラーが発生しました", e)
            false
        }

    }

    override fun deleteReaction(noteId: String): Boolean{
        return try{
            val data = ReactionCreateXorDeleteProperty( i = authKey, noteId = noteId)
            val json = jacksonObjectMapper().writeValueAsString(data)
            connection.postString(URL("$domain/api/notes/reactions/delete"), json) != null

        }catch(e: Exception){
            Log.w("", "error", e)
            false
        }
    }
}