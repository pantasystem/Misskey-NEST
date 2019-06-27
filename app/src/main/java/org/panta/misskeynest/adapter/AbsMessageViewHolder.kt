package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.util.ElapsedTimeFormatter
import org.panta.misskeynest.util.InjectionImage
import org.panta.misskeynest.util.InjectionText
import org.panta.misskeynest.viewdata.MessageViewData

/**
 * MessageとHistoryは似たようなところがあるので
 * 共通部分をこの抽象クラスでまとめる
 * 違う点は実装クラスで変化をつける
 */
abstract class AbsMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    abstract val messageTextView: TextView
    abstract val iconView: ImageView
    abstract val updatedAtView: TextView

    var itemClickListener: ItemClickListener<MessageViewData>? = null
    var userClickListener: ItemClickListener<User>? = null

    open fun onBind(item: MessageViewData){
        InjectionImage()
            .roundInjectionImage(item.message.user?.avatarUrl.toString(), iconView, 180)

        InjectionText()
            .injectionTextInvisible(item.message.text, messageTextView, null, null)

        updatedAtView.text = ElapsedTimeFormatter().formatTime(item.message.createdAt)

        itemView.setOnClickListener{
            itemClickListener?.onClick(item)
        }

        iconView.setOnClickListener{
            if(item.message.user != null){
                userClickListener?.onClick(item.message.user)
            }
        }

    }
}