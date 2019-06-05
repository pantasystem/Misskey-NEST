package org.panta.misskey_nest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_notification.view.*
import org.panta.misskey_nest.R
import org.panta.misskey_nest.constant.NotificationType
import org.panta.misskey_nest.constant.ReactionConstData
import org.panta.misskey_nest.entity.NotificationProperty

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val userIcon = itemView.notification_user_icon
    private val typeIcon = itemView.notification_status_icon
    private val userName = itemView.notification_user_name
    private val content = itemView.notification_content
    fun setNotification(property: NotificationProperty){
        picasso(userIcon, property.user.avatarUrl!!)
        when(NotificationType.getEnumFromString(property.type)){
            NotificationType.FOLLOW ->{
                typeIcon.setImageResource(R.drawable.human_icon)
                content.visibility = View.INVISIBLE
            }
            NotificationType.RENOTE ->{
                typeIcon.setImageResource(R.drawable.re_note_icon)
                content.text = property.note?.text
            }
            NotificationType.REACTION ->{
                setReactionTypeIcon(property.reaction)
                content.text = property.note?.text
            }
            else -> throw IllegalArgumentException("follow, renote, reactionしか対応していません。${property.type}")
        }

        userName.text = property.user.name
    }

    private fun setReactionTypeIcon(type: String?){
        when(type){
            ReactionConstData.RIP -> typeIcon.setImageResource(R.drawable.reaction_icon_rip)
            ReactionConstData.PUDDING -> typeIcon.setImageResource(R.drawable.reaction_icon_pudding)
            ReactionConstData.LOVE -> typeIcon.setImageResource(R.drawable.reaction_icon_love)
            ReactionConstData.LAUGH -> typeIcon.setImageResource(R.drawable.reaction_icon_laugh)
            ReactionConstData.ANGRY -> typeIcon.setImageResource(R.drawable.reaction_icon_angry)
            ReactionConstData.CONFUSED -> typeIcon.setImageResource(R.drawable.reaction_icon_confused)
            ReactionConstData.CONGRATS -> typeIcon.setImageResource(R.drawable.reaction_icon_congrats)
            ReactionConstData.HMM -> typeIcon.setImageResource(R.drawable.reaction_icon_hmm)
            ReactionConstData.LIKE -> typeIcon.setImageResource(R.drawable.reaction_icon_like)
            ReactionConstData.SURPRISE -> typeIcon.setImageResource(R.drawable.reaction_icon_surprise)
        }
    }

    private fun picasso(imageView: ImageView, url: String){
        Picasso
            .get()
            .load(url)
            .into(imageView)
    }
}