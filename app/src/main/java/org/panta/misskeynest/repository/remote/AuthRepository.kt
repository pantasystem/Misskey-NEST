package org.panta.misskeynest.repository.remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.SessionResponse
import org.panta.misskeynest.entity.UserKeyResponse
import org.panta.misskeynest.network.HttpsConnection
import java.net.URL
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