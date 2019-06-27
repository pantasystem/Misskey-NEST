package org.panta.misskeynest.usecase

import org.panta.misskeynest.interfaces.CallBackListener
import org.panta.misskeynest.viewdata.MessageViewData

interface IMessageChannelUseCase {
    var messageReceivedListener: CallBackListener<List<MessageViewData>>?
    fun start()
}