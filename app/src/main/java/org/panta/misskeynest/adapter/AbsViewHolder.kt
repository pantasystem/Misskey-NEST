package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbsViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView){

    abstract fun onBind(item: T)
}