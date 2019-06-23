package org.panta.misskeynest.repository.remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IAccountRepository
import java.net.URL

class AccountRepository(private val connectionProperty: ConnectionProperty): IAccountRepository{
    private val mConnection = OkHttpConnection()
    /*fun getMyInfo(callBack: (User?)->Unit){
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

    }*/

    override fun getMyInfo(): User? {

        val json = "{\"i\":\"${connectionProperty.i}\"}"
        val responseString = mConnection.postString(URL("${connectionProperty.domain}/api/i"), json)
        return if(responseString == null){
            null
        }else{
            jacksonObjectMapper().readValue<User>(responseString)
        }
    }
}