package org.panta.misskeynest.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.util.InjectionImage

class MessageHistoryViewHolder ( itemView: View ) : AbsMessageViewHolder( itemView ){

    override val messageTextView: TextView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val updatedAtView: TextView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val userIconView: ImageView
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun onBind(item: MessageProperty) {
        val recipientUser = item.recipient
            ?: throw IllegalArgumentException("Nullです")
        InjectionImage().roundInjectionImage(recipientUser.avatarUrl!!, userIconView, 180)
        messageTextView.text = item.text
    }


}