package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.DomainAndAppSecret
import org.panta.misskeynest.interfaces.ItemClickListener

class InstanceListAdapter(private val list: List<DomainAndAppSecret>) : RecyclerView.Adapter<org.panta.misskeynest.adapter.InstanceViewHolder>(){

    var clickListener: ItemClickListener<DomainAndAppSecret>? = null

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): org.panta.misskeynest.adapter.InstanceViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_instance, p0,false)
        return org.panta.misskeynest.adapter.InstanceViewHolder(inflater)
    }

    override fun onBindViewHolder(p0: org.panta.misskeynest.adapter.InstanceViewHolder, p1: Int) {
        val data = list[p1]
        p0.set(data)
        p0.clickListener = clickListener
    }
}