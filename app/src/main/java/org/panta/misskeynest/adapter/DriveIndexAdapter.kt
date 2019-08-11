package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_drive_index.view.*
import org.panta.misskeynest.R
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.viewdata.DriveViewData

class DriveIndexAdapter(private val list: List<DriveViewData.FolderViewData>) : RecyclerView.Adapter<DriveIndexAdapter.DriveIndexViewHolder>(){

    var itemClickListener: ItemClickListener<DriveViewData.FolderViewData>? = null

    class DriveIndexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val driveName = itemView.directory_name
        fun onBind(item: DriveViewData.FolderViewData){
            driveName.text = item.folder.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DriveIndexViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_drive_index, p0, false)
        return DriveIndexViewHolder(inflater)
    }

    override fun onBindViewHolder(p0: DriveIndexViewHolder, p1: Int) {
        p0.itemView.setOnClickListener{
            itemClickListener?.onClick(list[p1])
        }
        p0.onBind(list[p1])
    }


}