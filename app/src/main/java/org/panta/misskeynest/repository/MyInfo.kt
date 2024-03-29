package org.panta.misskeynest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.network.HttpsConnection
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.network.StreamConverter
import java.net.URL

class MyInfo(val domain: String, private val authKey: String){
    private val httpsConnection = OkHttpConnection()
    fun getMyInfo(callBack: (User?)->Unit){
        GlobalScope.launch{
            try{
                val json = "{\"i\":\"$authKey\"}"
                val responseString = httpsConnection.postString(URL("$domain/api/i"), json)
                if(responseString == null){
                    callBack(null)
                }else{
                    val obj = jacksonObjectMapper().readValue<User>(responseString)
                    callBack(obj)
                }

            }catch(e: Exception){
                Log.w("MyInfo", "getMyInfoでエラー発生", e)
            }
        }

    }
}