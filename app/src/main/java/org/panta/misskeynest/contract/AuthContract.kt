package org.panta.misskeynest.contract

import android.net.Uri
import org.panta.misskeynest.entity.SessionResponse
import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView

interface AuthContract {
    interface View : BaseView<Presenter> {
        fun showBrowser(uri: Uri)
        fun onLoadSession(session: SessionResponse)
        fun onLoadUserToken(token: String, domain: String)
    }

    interface Presenter : BasePresenter {

        //FIXME startで実行すればいいことなので不要か？
        fun getSession()
        fun getUserToken()
    }
}