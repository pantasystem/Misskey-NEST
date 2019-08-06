package org.panta.misskeynest.contract

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView
import org.panta.misskeynest.viewdata.MessageViewData
import java.io.File

interface MessageContract {
    interface Presenter : BasePresenter{
        fun getOldMessage()
        fun getNewMessage()
        fun sendMessage(text: String?)
        fun uploadFile(file: File)
        fun setFile(file: FileProperty)
    }

    interface View : BaseView<Presenter>{
        fun showMessage(list: List<MessageViewData>)
        fun showOldMessage(list: List<MessageViewData>)
        fun showNewMessage(list: List<MessageViewData>)
        fun showRecievedMessage(list: List<MessageViewData>)
        fun showFileManager()
        fun onUploadFile(file: FileProperty?)
        fun showSuccessSendMessage()
        fun showFailureSendMessage()
    }
}

