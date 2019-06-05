package org.panta.misskey_nest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.constant.getInstanceInfoList
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.entity.SessionResponse
import org.panta.misskey_nest.entity.UserKeyResponse
import org.panta.misskey_nest.interfaces.ISharedPreferenceOperator
import org.panta.misskey_nest.network.HttpsConnection
import org.panta.misskey_nest.storage.SharedPreferenceOperator
import org.panta.misskey_nest.util.sha256
import java.lang.StringBuilder
import java.net.URL
import java.security.MessageDigest
class AuthRepository(private val domain: String, private val appSecret: String){



    private val mapper = jacksonObjectMapper()
    private val connection = HttpsConnection()

    fun getSession(callBack: (SessionResponse)->Unit) = GlobalScope.launch {
        try{
            val map = HashMap<String, String>()
            map["appSecret"] = appSecret
            val json = mapper.writeValueAsString(map)
            val stream = connection.post(URL("$domain/api/auth/session/generate"), json)
            val session: SessionResponse = mapper.readValue(stream)
            callBack(session)
        }catch(e: Exception){

        }

    }

    fun getUserToken(token: String, errorCallBack: (e: Exception)->Unit, callBack: (UserKeyResponse) -> Unit) = GlobalScope.launch{
        try{

            val map = HashMap<String, String>()
            map["appSecret"] = appSecret
            map["token"] = token
            val json = mapper.writeValueAsString(map)

            val stream = connection.post(URL("$domain/api/auth/session/userkey"), json)
            val userKey : UserKeyResponse = mapper.readValue(stream)
            //val i = sha256("${userKey.accessToken}$appSecret")
            //Log.d("AuthRepository", "i: $i")
            callBack(userKey)
        }catch(e: Exception){
            errorCallBack(e)
        }
    }


}