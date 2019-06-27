package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.MessageDataType
import org.panta.misskeynest.viewdata.MessageViewData

class MessageFilter(private val mConnectionProperty: ConnectionProperty) : IItemFilter<MessageProperty, MessageViewData>{
    override fun filter(items: List<MessageProperty>): List<MessageViewData> {
        return items.map{
            filter(it)
        }
    }

    //FIXME Streaming APIを用いるときにrecipientはNULLではないのでHistoryと判定されてしまう
    override fun filter(item: MessageProperty): MessageViewData{
        val isOwn = mConnectionProperty.userPrimaryId == item.userId
        return when{
            item.group != null ->{
                val title = item.group.name
                MessageViewData(item.id, false, item, MessageDataType.HISTORY_GROUP, isOwn, title)
            }
            item.recipient != null ->{
                val title = item.user?.name
                MessageViewData(item.id, false, item, MessageDataType.HISTORY_USER, isOwn, title)
            }
            item.recipientId == null->{
                MessageViewData( item.id, false, item, MessageDataType.MESSAGE_GROUP, isOwn, null)
            }
            item.groupId == null ->{
                MessageViewData( item.id, false, item, MessageDataType.MESSAGE_USER, isOwn, null)
            }
            else ->{
                throw IllegalArgumentException("不正な値")
            }

        }
    }
}