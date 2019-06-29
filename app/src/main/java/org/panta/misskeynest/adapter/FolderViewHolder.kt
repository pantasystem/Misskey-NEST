package org.panta.misskeynest.adapter

import android.view.View
import android.widget.TextView
import org.panta.misskeynest.R
import org.panta.misskeynest.viewdata.DriveViewData

class FolderViewHolder(itemView: View) : AbsViewHolder<DriveViewData>(itemView){

    private val folderName: TextView = itemView.findViewById(R.id.folder_name)
    override fun onBind(item: DriveViewData) {
        if(item is DriveViewData.FolderViewData){
            folderName.text = item.folder.name
        }
    }
}