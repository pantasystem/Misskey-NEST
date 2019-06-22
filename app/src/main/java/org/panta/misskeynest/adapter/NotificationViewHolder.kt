package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_notification.view.*
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.NotificationType
import org.panta.misskeynest.constant.ReactionConstData
import org.panta.misskeynest.entity.NotificationProperty
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.interfaces.IUserClickListener

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val userIcon = itemView.notification_user_icon
    private val typeIcon = itemView.notification_status_icon
    private val userName = itemView.notification_user_name
    private val content = itemView.notification_content

    var noteClickListener: INoteClickListener? = null
    var userClickListener: IUserClickListener? = null

    fun setNotification(property: NotificationProperty){
        picasso(userIcon, property.user.avatarUrl!!)
        when(NotificationType.getEnumFromString(property.type)){
            NotificationType.FOLLOW ->{
                typeIcon.setImageResource(R.drawable.ic_human)
                content.visibility = View.INVISIBLE
            }
            NotificationType.RENOTE ->{
                typeIcon.setImageResource(R.drawable.ic_re_note)
                content.text = property.note?.renote?.text
            }
            NotificationType.REACTION ->{
                setReactionTypeIcon(property.reaction)
                content.text = property.note?.text
            }
            else -> throw IllegalArgumentException("follow, renote, reactionしか対応していません。${property.type}")
        }
        userName.setOnClickListener {
            userClickListener?.onClickedUser(property.user)
        }

        userIcon.setOnClickListener {
            userClickListener?.onClickedUser(property.user)
        }

        content.setOnClickListener {
            if(property.note != null){
                noteClickListener?.onNoteClicked(property.note)
            }
        }

        userName.text = property.user.name
    }

    private fun setReactionTypeIcon(type: String?){
        when(type){
            ReactionConstData.RIP -> typeIcon.setImageResource(R.drawable.ic_reaction_rip)
            ReactionConstData.PUDDING -> typeIcon.setImageResource(R.drawable.ic_reaction_pudding)
            ReactionConstData.LOVE -> typeIcon.setImageResource(R.drawable.ic_reaction_love)
            ReactionConstData.LAUGH -> typeIcon.setImageResource(R.drawable.ic_reaction_laugh)
            ReactionConstData.ANGRY -> typeIcon.setImageResource(R.drawable.ic_reaction_angry)
            ReactionConstData.CONFUSED -> typeIcon.setImageResource(R.drawable.ic_reaction_confused)
            ReactionConstData.CONGRATS -> typeIcon.setImageResource(R.drawable.ic_reaction_congrats)
            ReactionConstData.HMM -> typeIcon.setImageResource(R.drawable.ic_reaction_hmm)
            ReactionConstData.LIKE -> typeIcon.setImageResource(R.drawable.ic_reaction_like)
            ReactionConstData.SURPRISE -> typeIcon.setImageResource(R.drawable.ic_reaction_surprise)
        }
    }

    private fun picasso(imageView: ImageView, url: String){
        Picasso
            .get()
            .load(url)
            .into(imageView)
    }
}