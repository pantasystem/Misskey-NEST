package org.panta.misskey_nest.interfaces

import android.net.Uri
import org.panta.misskey_nest.entity.SessionResponse
import java.net.URI

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