package org.panta.misskeynest.repository.remote

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.network.OkHttpConnection
import java.net.URL

class UserRepository(private val domain: String, private val authKey: String){

    private val httpsConnection = OkHttpConnection()

    fun getUserInfoByUserName(userName: String, host: String?, callBack: (User?)-> Unit){
        GlobalScope.launch {
            try{
                val map = hashMapOf("i" to authKey, "username" to userName)
                if(host != null){
                    map["host"] = host
                }
                //Log.d("", "$map")
                val json = jacksonObjectMapper().writeValueAsString(map)
                //Log.d("", json)
                val res = httpsConnection.postString(URL("$domain/api/users/show"), json)
                if(res == null){
                    callBack(null)
                }else{
                    callBack(jacksonObjectMapper().readValue(res))
                }
            }catch (e: Exception){
                Log.d("UserRepository", "", e)
            }
        }
    }

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