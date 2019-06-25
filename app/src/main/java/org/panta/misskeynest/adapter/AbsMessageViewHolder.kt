package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

    abstract fun onBind(item: MessageViewData)
}