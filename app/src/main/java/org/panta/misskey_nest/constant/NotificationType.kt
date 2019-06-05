package org.panta.misskey_nest.constant

enum class NotificationType{
    FOLLOW, MENTION, REPLY, RENOTE, QUOTE, REACTION, POLL_VOTE, RECEIVE_FOLLOW_REQUEST;
    companion object {
        fun getEnumFromString(string: String): NotificationType{
            return when(string){
                NotificationConstant.FOLLOW -> FOLLOW
                NotificationConstant.MENTION -> MENTION
                NotificationConstant.REPLY -> REPLY
                NotificationConstant.RENOTE -> RENOTE
                NotificationConstant.QUOTE -> QUOTE
                NotificationConstant.REACTION -> REACTION
                NotificationConstant.POLL_VOTE -> POLL_VOTE
                NotificationConstant.RECEIVE_FOLLOW_REQUEST -> RECEIVE_FOLLOW_REQUEST
                else -> throw IllegalArgumentException("定数に一致しませんでした。")
            }
        }
    }
}