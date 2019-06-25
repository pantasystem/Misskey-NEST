package org.panta.misskeynest.usecase

import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.MessageViewData

interface IHistoryUseCase{
    val mFilter: IItemFilter<MessageProperty, MessageViewData>

    fun getHistory(callBack:(List<MessageViewData>?)-> Unit)
    fun getGroupHistory(callBack: (List<MessageViewData>?)->Unit)
    fun getMixHistory(callBack: (List<MessageViewData>?)-> Unit)
}