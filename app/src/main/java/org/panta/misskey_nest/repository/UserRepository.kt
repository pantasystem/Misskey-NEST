package org.panta.misskey_nest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.entity.FollowProperty
import org.panta.misskey_nest.entity.User
import org.panta.misskey_nest.network.HttpsConnection
import org.panta.misskey_nest.network.OkHttpConnection
import org.panta.misskey_nest.network.StreamConverter
import java.net.URL

class UserRepository(private val domain: String, private val authKey: String){

    private val httpsConnection = OkHttpConnection()
    fun getUserInfo(userPrimaryId: String, callBack: (User?)->Unit){
        GlobalScope.launch{
            try{
                val json = "{\"userId\":\"$userPrimaryId\"}"
                val responseJson = httpsConnection.postString(URL("$domain/api/users/show"), json)
                Log.d("UserRepository", responseJson)
                if(responseJson == null){
                    callBack(null)
                }else{
                    val obj = jacksonObjectMapper().readValue<User>(responseJson)
                    callBack(obj)
                }

            }catch(e: Exception){
                Log.w("UserRepository", "getUserInfoでエラー発生", e)
            }
        }
    }

    fun followUser(userId: String, callBack: (Boolean)->Unit){
        val map = HashMap<String, String>()
        map["i"] = authKey
        map["userId"] = userId
        GlobalScope.launch{
            try{
                val json = jacksonObjectMapper().writeValueAsString(map)
                val response = httpsConnection.postString(URL("$domain/api/following/create"), json)
                callBack(response != null)
            }catch(e: Exception){
                callBack(false)
                Log.w("UserRepository", "error", e)
            }
        }
    }

    fun unFollowUser(userId: String, callBack: (Boolean) -> Unit){
        val map = mapOf("i" to authKey, "userId" to userId)
        GlobalScope.launch{
            try{
                val json = jacksonObjectMapper().writeValueAsString(map)
                val response = httpsConnection.postString(URL("$domain/api/following/delete"), json)
                callBack( response != null)
            }catch(e: Exception){
                callBack(false)
                Log.w("UserRepository", "error", e)

            }
        }
    }

}