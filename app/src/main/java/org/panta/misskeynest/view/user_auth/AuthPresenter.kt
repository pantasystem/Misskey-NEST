package org.panta.misskeynest.view.user_auth

import org.panta.misskeynest.constant.getInstanceInfoList
import org.panta.misskeynest.interfaces.AuthContract
import org.panta.misskeynest.repository.AuthRepository
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator

class AuthPresenter(private val mView: AuthContract.View, private val sharedPref: SharedPreferenceOperator, domain: String?, appSecret: String?) : AuthContract.Presenter{

    private val secretRepository = PersonalRepository(sharedPref)
    private val authRepository = if(domain == null || appSecret == null){
        val tmpDomain = sharedPref.getString("tmpDomain", null)
        val appSecretKey = getInstanceInfoList().first {
            it.domain == tmpDomain
        }.appSecret
        AuthRepository(domain = tmpDomain!!, appSecret = appSecretKey)
    }else{
        AuthRepository(domain = domain, appSecret = appSecret)
    }

    private var mDomain: String = domain ?: sharedPref.getString("tmpDomain", null)!!

    override fun getSession() {
        authRepository.getSession {
            sharedPref.putString("tmpSession", it.token)
            sharedPref.putString("tmpDomain", mDomain)
            mView.onLoadSession(it)
        }
    }

    override fun getUserToken() {
        val session = sharedPref.getString("tmpSession", null)
        val tmpDomain = sharedPref.getString("tmpDomain", null)
        if(session != null && tmpDomain != null){
            authRepository.getUserToken(session, {}){
                mView.onLoadUserToken(it.accessToken, tmpDomain)
                //sharedPref.put(ApplicationConstant.APP_USER_TOKEN_KEY, it)
                //sharedPref.put(ApplicationConstant.APP_DOMAIN_KEY, tmpDomain)
                //authRepository.putUserToken(it)

                secretRepository.putUserToken(it.accessToken)
                secretRepository.putDomain(tmpDomain)
                secretRepository.putUserPrimaryId(it.user.id)
            }
        }else{
            getSession()
        }
    }



    override fun start() {

    }
}