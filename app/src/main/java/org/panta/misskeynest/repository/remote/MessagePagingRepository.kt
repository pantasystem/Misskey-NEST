package org.panta.misskeynest.repository.remote

import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.repository.IItemRepository

class MessagePagingRepository(connectionProperty: ConnectionProperty, private val userId: String?, private val groupId:String?) : IItemRepository<MessageProperty>{

    private val mMessage = MessageRepository(connectionProperty)

    override fun getItems(): List<MessageProperty>? {
        return mMessage.getMessages(groupId = groupId, userId = userId, untilId = null, sinceId = null)
    }

    override fun getItemsUseSinceId(sinceId: String): List<MessageProperty>? {
        return mMessage.getMessages(groupId = groupId, userId = userId, untilId = null, sinceId = sinceId)

    }

    override fun getItemsUseUntilId(untilId: String): List<MessageProperty>? {
        return mMessage.getMessages(groupId = groupId, userId = userId, untilId = untilId, sinceId = null)

    }

}