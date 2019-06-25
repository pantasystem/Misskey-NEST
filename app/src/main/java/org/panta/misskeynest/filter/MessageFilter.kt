package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.MessageDataType
import org.panta.misskeynest.viewdata.MessageViewData

class MessageFilter : IItemFilter<MessageProperty, MessageViewData>{
    override fun filter(items: List<MessageProperty>): List<MessageViewData> {
        return items.map{
            filter(it)
        }
    }

    fun filter(item: MessageProperty): MessageViewData{
        return when{
            item.group != null ->{
                MessageViewData(item.id, false, item, MessageDataType.HISTORY_GROUP)
            }
            item.recipient != null ->{
                MessageViewData(item.id, false, item, MessageDataType.HISTORY_USER)
            }
            else ->{
                MessageViewData(item.id, false, item, MessageDataType.MESSAGE)
            }

        }
    }
}