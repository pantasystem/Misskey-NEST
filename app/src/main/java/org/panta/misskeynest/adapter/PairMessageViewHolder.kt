package org.panta.misskeynest.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_message_partner.view.*
import org.panta.misskeynest.viewdata.MessageViewData

class PairMessageViewHolder( itemView: View) : AbsMessageViewHolder(itemView){

    override val iconView: ImageView = itemView.user_icon
    override val messageTextView: TextView = itemView.message_text
    override val updatedAtView: TextView = itemView.elapsed_time
    override val imageView: ImageView = itemView.message_image

    private val userName = itemView.user_name

    override fun onBind(item: MessageViewData) {
        super.onBind(item)
        userName.text = item.message.user?.name?: item.message.user?.userName
    }
}