package org.panta.misskeynest.viewdata

import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.ID

enum class MessageDataType{
    HISTORY_GROUP,
    HISTORY_USER,
    MESSAGE_GROUP,
    MESSAGE_USER
}
data class MessageViewData(override val id: String, override val isIgnore: Boolean, val message: MessageProperty, val messageType: MessageDataType, val isOwn: Boolean) : ID