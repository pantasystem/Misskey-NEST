package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.viewdata.MessageViewData

class MessageHistoryAdapter(private val list: List<MessageViewData>) : RecyclerView.Adapter<AbsMessageViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AbsMessageViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_history, p0, false)
        return MessageHistoryViewHolder(inflater)
    }

    override fun onBindViewHolder(p0: AbsMessageViewHolder, p1: Int) {

        p0.onBind(list[p1])
    }
}
