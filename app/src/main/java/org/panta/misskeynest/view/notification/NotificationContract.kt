package org.panta.misskeynest.view.notification

import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView
import org.panta.misskeynest.viewdata.NotificationViewData

interface NotificationContract {
    interface View : BaseView<Presenter>{
        fun showOldNotification(list: List<NotificationViewData>)
        fun showNewNotification(list: List<NotificationViewData>)
        fun showInitNotification(list: List<NotificationViewData>)
        fun stopRefreshing()
        fun onError(errorMsg: String)
    }

    interface Presenter : BasePresenter{
        fun getOldNotification()
        fun getNewNotification()
        fun initNotification()
        fun markAllAsRead()
    }
}