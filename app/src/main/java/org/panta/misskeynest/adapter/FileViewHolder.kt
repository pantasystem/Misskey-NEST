package org.panta.misskeynest.adapter

import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_drive_file.view.*
import org.panta.misskeynest.util.ElapsedTimeFormatter
import org.panta.misskeynest.util.InjectionImage
import org.panta.misskeynest.viewdata.DriveViewData

class FileViewHolder(itemView: View) : AbsViewHolder<DriveViewData>(itemView){

    private val thumbnailIcon: ImageView = itemView.thumbnail_view
    private val fileName = itemView.file_name
    private val fileMeta = itemView.file_meta
    private val fileSize = itemView.file_size
    private val updatedTime = itemView.updated_at


    override fun onBind(item: DriveViewData) {
        if( item is DriveViewData.FileViewData){
            val property = item.fileProperty

            if(property.thumbnalUrl == null){
                thumbnailIcon.visibility = View.INVISIBLE
            }else{
                InjectionImage().injectionImage(property.thumbnalUrl, thumbnailIcon, property.isSensitive)
            }

            fileName.text = property.name
            fileMeta.text = property.type
            fileSize.text = java.lang.String.valueOf(property.size)
            updatedTime.text = ElapsedTimeFormatter().formatTime(property.createdAt!!)

        }
    }
}