package org.panta.misskeynest.streaming

import org.panta.misskeynest.interfaces.CallBackListener
import org.panta.misskeynest.viewdata.MessageViewData

interface IMessageChannelUseCase {
    var messageReceivedListener: CallBackListener<List<MessageViewData>>?
    fun start()
}