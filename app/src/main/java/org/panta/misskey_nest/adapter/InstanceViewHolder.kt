package org.panta.misskey_nest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_instance.view.*
import org.panta.misskey_nest.constant.DomainAndAppSecret
import org.panta.misskey_nest.interfaces.ItemClickListener

class InstanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val instanceName = itemView.instance_name
    private val domain = itemView.domain
    var clickListener: ItemClickListener<DomainAndAppSecret>? = null
    fun set(data: DomainAndAppSecret){
        instanceName.text = data.instanceName
        domain.text = data.domain

        itemView.setOnClickListener{
            clickListener?.onClick(data)
        }
    }
}