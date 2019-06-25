package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.MessageDataType
import org.panta.misskeynest.viewdata.MessageViewData

class MessageFilter(private val mConnectionproperty: ConnectionProperty) : IItemFilter<MessageProperty, MessageViewData>{
    override fun filter(items: List<MessageProperty>): List<MessageViewData> {
        return items.map{
            filter(it)
        }
    }

    fun filter(item: MessageProperty): MessageViewData{
        val isOwn = mConnectionproperty.userPrimaryId == item.userId
        return when{
            item.group != null ->{
                MessageViewData(item.id, false, item, MessageDataType.HISTORY_GROUP, isOwn)
            }
            item.recipient != null ->{
                MessageViewData(item.id, false, item, MessageDataType.HISTORY_USER, isOwn)
            }
            item.recipientId == null->{
                MessageViewData( item.id, false, item, MessageDataType.MESSAGE_GROUP, isOwn)
            }
            item.groupId == null ->{
                MessageViewData( item.id, false, item, MessageDataType.MESSAGE_USER, isOwn)
            }
            else ->{
                throw IllegalArgumentException("不正な値")
            }

        }
    }
}