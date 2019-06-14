package org.panta.misskeynest.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.view_data.NoteViewData

const val ITEM_TYPE_NORMAL = 0
const val ITEM_TYPE_DETAIL = 1
class NoteDetailAdapter(private val list: List<NoteViewData>, private val currentId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if( list[position].id == currentId ) ITEM_TYPE_DETAIL else ITEM_TYPE_NORMAL
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return if( p1 == ITEM_TYPE_NORMAL ){
            val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0)
            NoteViewHolder(inflater, LinearLayoutManager(p0.context))
        }else{
            val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_detailed_note, p0)
            DetailNoteViewHolder(inflater)
        }
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        when(p0){
            is NoteViewHolder ->{

            }
            is DetailNoteViewHolder ->{

            }
            else -> throw IllegalAccessException("NoteViewHolder、DetailNoteViewHolder以外を使用することはできない")
        }
    }

}