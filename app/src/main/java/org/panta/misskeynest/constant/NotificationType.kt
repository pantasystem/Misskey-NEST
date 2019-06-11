package org.panta.misskeynest.constant

enum class NotificationType{
    FOLLOW, MENTION, REPLY, RENOTE, QUOTE, REACTION, POLL_VOTE, RECEIVE_FOLLOW_REQUEST;
    companion object {
        fun getEnumFromString(string: String): NotificationType{
            return when(string){
                org.panta.misskeynest.constant.NotificationConstant.FOLLOW -> FOLLOW
                org.panta.misskeynest.constant.NotificationConstant.MENTION -> MENTION
                org.panta.misskeynest.constant.NotificationConstant.REPLY -> REPLY
                org.panta.misskeynest.constant.NotificationConstant.RENOTE -> RENOTE
                org.panta.misskeynest.constant.NotificationConstant.QUOTE -> QUOTE
                org.panta.misskeynest.constant.NotificationConstant.REACTION -> REACTION
                org.panta.misskeynest.constant.NotificationConstant.POLL_VOTE -> POLL_VOTE
                org.panta.misskeynest.constant.NotificationConstant.RECEIVE_FOLLOW_REQUEST -> RECEIVE_FOLLOW_REQUEST
                else -> throw IllegalArgumentException("定数に一致しませんでした。")
            }
        }
    }
}