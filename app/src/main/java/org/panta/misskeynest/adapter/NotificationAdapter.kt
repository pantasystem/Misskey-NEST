package org.panta.misskeynest.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.NotificationType
import org.panta.misskeynest.emoji.CustomEmoji
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.usecase.NoteAdjustment
import org.panta.misskeynest.view_data.NotificationViewData
import java.io.File

class NotificationAdapter(private val notificationList: List<NotificationViewData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IOperationAdapter<NotificationViewData>{
    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return NotificationType.getEnumFromString(notificationList[position].notificationProperty.type).ordinal
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {



        val isNotification = viewType == NotificationType.FOLLOW.ordinal|| viewType ==  NotificationType.RENOTE.ordinal || viewType == NotificationType.REACTION.ordinal
        return if(isNotification){
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_notification, p0,false)
            NotificationViewHolder(view)
        }else{
            //Log.d("NotificationAdapter", "onCreateViewHolder params:$notificationType")
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)

            NoteViewHolder(view, null, CustomEmoji(p0.context))
        }


    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, p1: Int) {
        val data = notificationList[p1]
        if(viewHolder is NotificationViewHolder){
            viewHolder.setNotification(data.notificationProperty)
        }else if(viewHolder is NoteViewHolder){
            val viewData = data.noteViewData
            viewHolder.invisibleReactionCount()
            when{
                viewData!!.type == NoteAdjustment.NoteType.REPLY -> {
                    viewHolder.setReply(viewData)
                }
                viewData.type == NoteAdjustment.NoteType.REPLY_TO ->{
                    viewHolder.setReplyTo(viewData)
                }
                viewData.type == NoteAdjustment.NoteType.NOTE -> {
                    //これはNote
                    viewHolder.setNote(viewData)
                }
                viewData.type == NoteAdjustment.NoteType.RE_NOTE -> {
                    //これはリノート
                    viewHolder.setReNote(viewData)
                }
                viewData.type == NoteAdjustment.NoteType.QUOTE_RE_NOTE -> {
                    viewHolder.setQuoteReNote(viewData)
                }
            }
            viewHolder.setNote(data.noteViewData!!)
        }
    }

    override fun addAllFirst(list: List<NotificationViewData>) {
        if(notificationList is ArrayList){
            synchronized(notificationList){
                notificationList.addAll(0, list)
            }
            Handler().post{
                notifyItemRangeInserted(0, list.size)
            }
        }
    }

    override fun addAllLast(list: List<NotificationViewData>) {
        if(notificationList is ArrayList){
            val lastIndex = notificationList.size
            synchronized(notificationList){
                notificationList.addAll(list)
            }
            Handler().post{
                //実験段階不具合の可能性有り
                notifyItemRangeInserted(lastIndex, list.size)
            }
        }
    }

    override fun getItem(index: Int): NotificationViewData {
        synchronized(notificationList){
            return notificationList[index]
        }
    }

    override fun getItem(item: NotificationViewData): NotificationViewData {
        synchronized(notificationList){
            return notificationList.first { it.id == item.id }
        }
    }

    override fun removeItem(item: NotificationViewData) {
        synchronized(notificationList){
            val index = notificationList.indexOf(item)
            if(notificationList is ArrayList){
                notificationList[index] = item

                Handler().post{
                    notifyItemChanged(index)
                }
            }
        }
    }

    override fun updateItem(item: NotificationViewData) {
        synchronized(notificationList){
            val index = notificationList.indexOf(item)
            if(notificationList is ArrayList){
                notificationList.remove(item)

                Handler().post{
                    notifyItemRemoved(index)
                }
            }
        }
    }

}