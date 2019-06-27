package org.panta.misskeynest.contract

import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView
import org.panta.misskeynest.viewdata.MessageViewData

interface MessageContract {
    interface Presenter : BasePresenter{
        fun getOldMessage()
        fun getNewMessage()
    }

    interface View : BaseView<Presenter>{
        fun showMessage(list: List<MessageViewData>)
        fun showOldMessage(list: List<MessageViewData>)
        fun showNewMessage(list: List<MessageViewData>)
        fun showRecievedMessage(list: List<MessageViewData>)
    }
}

