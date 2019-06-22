package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import org.panta.misskeynest.viewdata.IMAGE_NOTE
import org.panta.misskeynest.viewdata.OverviewViewData
import org.panta.misskeynest.viewdata.PIN_NOTE
import org.panta.misskeynest.viewdata.RECENT_NOTE

class OverviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun onBind(item: OverviewViewData){
        when( item.type ){
            PIN_NOTE ->{

            }
            RECENT_NOTE ->{
                /*val lm = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                val adapter = NoteView*/
            }
            IMAGE_NOTE ->{

            }
            else ->{

            }
        }
    }
}