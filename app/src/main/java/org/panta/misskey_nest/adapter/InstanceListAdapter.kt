package org.panta.misskey_nest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskey_nest.R
import org.panta.misskey_nest.constant.DomainAndAppSecret
import org.panta.misskey_nest.interfaces.ItemClickListener

class InstanceListAdapter(private val list: List<DomainAndAppSecret>) : RecyclerView.Adapter<InstanceViewHolder>(){

    var clickListener: ItemClickListener<DomainAndAppSecret>? = null

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InstanceViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_instance, p0,false)
        return InstanceViewHolder(inflater)
    }

    override fun onBindViewHolder(p0: InstanceViewHolder, p1: Int) {
        val data = list[p1]
        p0.set(data)
        p0.clickListener = clickListener
    }
}