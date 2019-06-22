package org.panta.misskeynest.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.viewdata.OverviewViewData
import org.panta.misskeynest.viewdata.PIN_NOTE

class OverviewAdapter(private val list: List<OverviewViewData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):RecyclerView.ViewHolder {
        when(p1){
            PIN_NOTE ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
                return NoteViewHolder(inflater, LinearLayoutManager(p0.context))
            }

        }
        TODO("実装する")
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

        when( p0 ){
            is NoteViewHolder ->{
                p0.onBind(list[p1].item!!)
            }
            //is
        }
    }

}