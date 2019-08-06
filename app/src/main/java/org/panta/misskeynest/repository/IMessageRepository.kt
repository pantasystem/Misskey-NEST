package org.panta.misskeynest.repository

import org.panta.misskeynest.entity.MessageProperty

interface IMessageRepository {
    fun getHistory(isGroup: Boolean): List<MessageProperty>?
    fun create(userId: String?, groupId: String?, text: String?, fileId: String?): Boolean
    fun getMessages(userId: String?, groupId: String?, untilId: String?, sinceId: String?): List<MessageProperty>?
}