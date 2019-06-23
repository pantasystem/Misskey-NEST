package org.panta.misskeynest.contract

import android.net.Uri
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView

interface MainContract {
    interface View : BaseView<Presenter> {
        fun showPersonalMiniProfile(user: User)
        fun showPersonalProfilePage(user: User, connectionInfo: ConnectionProperty)
        fun showAuthActivity()
        fun initDisplay(connectionInfo: ConnectionProperty)
        fun showEditNote(connectionInfo: ConnectionProperty)
        fun showFollowFollower(connectionInfo: ConnectionProperty, user: User, type: FollowFollowerType)
        fun showMisskeyOnBrowser(url: Uri)
        fun showIsEnabledNotification(enabled: Boolean)
        fun startNotificationService()
        fun stopNotificationService()
    }

    interface Presenter : BasePresenter {
        fun getPersonalMiniProfile()
        fun getPersonalProfilePage()
        //fun saveConnectInfo(domain: String, userToken:String)
        fun initDisplay()
        fun takeEditNote()
        fun getFollowFollower(type: FollowFollowerType)
        fun openMisskeyOnBrowser()
        fun isEnabledNotification(enabled: Boolean? = null)
        fun sendNote(text: String)
    }
}