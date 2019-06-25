package org.panta.misskeynest.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_message_history.view.*
import org.panta.misskeynest.util.ElapsedTimeFormatter
import org.panta.misskeynest.util.InjectionImage
import org.panta.misskeynest.viewdata.MessageDataType
import org.panta.misskeynest.viewdata.MessageViewData

class MessageHistoryViewHolder ( itemView: View ) : AbsMessageViewHolder( itemView ){

    override val messageTextView: TextView = itemView.history_message

    override val updatedAtView: TextView = itemView.updated_at


    override val iconView: ImageView = itemView.history_icon


    private val historyTitle = itemView.history_title

    override fun onBind(item: MessageViewData) {

        messageTextView.text = item.message.text
        updatedAtView.text = ElapsedTimeFormatter().formatTime(item.message.createdAt)

        when(item.messageType){
            MessageDataType.HISTORY_USER ->{
                val user = item.message.recipient!!
                InjectionImage()
                    .roundInjectionImage(user.avatarUrl.toString(), iconView, 180)
                historyTitle.text = user.name?: user.userName
            }
            MessageDataType.HISTORY_GROUP ->{
                val group = item.message.group!!
                val iconUrl = item.message.user?.avatarUrl.toString()
                InjectionImage()
                    .roundInjectionImage(iconUrl, iconView, 180)

                historyTitle.text = group.name
            }
            else -> throw IllegalArgumentException( "これはHistory用のVHでありMessageは許可されていません。" )
        }
    }


}