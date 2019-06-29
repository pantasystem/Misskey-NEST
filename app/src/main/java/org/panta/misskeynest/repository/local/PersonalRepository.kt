package org.panta.misskeynest.repository.local

import org.panta.misskeynest.constant.ThemeType
import org.panta.misskeynest.constant.getInstanceInfoList
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.repository.ISharedPreferenceOperator
import org.panta.misskeynest.util.sha256

class PersonalRepository(private val sharedPreferenceOperator: ISharedPreferenceOperator){

    companion object{
        private const val APP_DOMAIN = "misskey_account_domain"
        private const val APP_USER_TOKEN = "misskey_account_user_token"
        private const val APP_USER_PRIMARY_ID = "misskey_account_primary_id"
        private const val APP_THEME_KEY = "misskey_theme_id"
    }

    fun getDomain(): String?{
        return sharedPreferenceOperator.getString(APP_DOMAIN, null)
    }

    fun getUserToken(): String?{
        return sharedPreferenceOperator.getString(APP_USER_TOKEN, null)
    }

    fun getUserPrimaryId(): String?{
        return sharedPreferenceOperator.getString(APP_USER_PRIMARY_ID, null)
    }

    fun getConnectionInfo(): ConnectionProperty?{
        val domain = getDomain()
        val userToken = getUserToken()
        val userPrimaryId = getUserPrimaryId()

        val instanceInfoList = getInstanceInfoList()
        val appSecret = instanceInfoList.firstOrNull{
            it.domain == domain
        }?.appSecret
        return if(appSecret == null || domain == null || userToken == null || userPrimaryId == null){
            return null
        }else{
            val i =  sha256("$userToken$appSecret")
            ConnectionProperty(domain, i, userPrimaryId)
        }
        /*return if(appSecret != null && domain != null && userToken != null){
            val i =  sha256("$userToken$appSecret")
            ConnectionProperty(domain, i)
        }else{
            null
        }*/
    }

    fun getUserTheme(): ThemeType{
        val n = sharedPreferenceOperator.getString(APP_THEME_KEY, 0.toString())
        if(n == null){
            return ThemeType.STANDARD
        }else{
            val typeNumber = Integer.parseInt(n)
            return ThemeType.getThemeTypeFromInt(typeNumber)
        }

    }

    fun putDomain(domain: String){
        sharedPreferenceOperator.putString(APP_DOMAIN, domain)
    }

    fun putUserToken(token: String){
        sharedPreferenceOperator.putString(APP_USER_TOKEN, token)
    }

    fun putUserPrimaryId(id: String){
        sharedPreferenceOperator.putString(APP_USER_PRIMARY_ID, id)
    }

    fun putUserTheme(type: ThemeType){
        sharedPreferenceOperator.putString(APP_THEME_KEY, type.ordinal.toString())
    }


}