package org.panta.misskeynest.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_message_self.view.*

class SelfMessageViewHolder(itemView: View) : AbsMessageViewHolder(itemView){

    override val iconView: ImageView = itemView.user_icon
    override val messageTextView: TextView = itemView.message_text
    override val updatedAtView: TextView = itemView.elapsed_time

}

