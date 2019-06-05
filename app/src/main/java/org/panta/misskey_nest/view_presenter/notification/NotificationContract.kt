package org.panta.misskey_nest.view_presenter.notification

import org.panta.misskey_nest.entity.NotificationProperty
import org.panta.misskey_nest.interfaces.BasePresenter
import org.panta.misskey_nest.interfaces.BaseView
import org.panta.misskey_nest.view_data.NotificationViewData

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