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
import org.panta.misskeynest.usecase.ShowReaction
import org.panta.misskeynest.util.getEmojiPathFromName

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val userIcon = itemView.notification_user_icon
    private val typeIcon = itemView.notification_status_icon
    private val typeTextIcon = itemView.notification_status_text_icon

    private val userName = itemView.notification_user_name
    private val content = itemView.notification_content

    var noteClickListener: INoteClickListener? = null
    var userClickListener: IUserClickListener? = null

    fun setNotification(property: NotificationProperty){
        picasso(userIcon, property.user.avatarUrl!!)
        when(NotificationType.getEnumFromString(property.type)){
            NotificationType.FOLLOW ->{
                typeIcon.setImageResource(R.drawable.ic_human)
                typeTextIcon.visibility = View.GONE
                content.visibility = View.INVISIBLE
            }
            NotificationType.RENOTE ->{
                typeIcon.setImageResource(R.drawable.ic_re_note)
                typeTextIcon.visibility = View.GONE
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

        if(type == null)return
        val resourceId = ReactionConstData.getDefaultReactionIconMapping()[type]
        val emojiFile = getEmojiPathFromName(itemView.context, type.replace(":", ""))
        val showReaction = ShowReaction(typeIcon, typeTextIcon)

        if(resourceId == null && emojiFile == null){
            //全てに当てはまらない場合
            showReaction.setTextReaction(type)
        }else if(resourceId != null){
            //定数画像に含まれる場合
            showReaction.setReactionFromResource(resourceId)
        }else if(emojiFile != null){
            showReaction.setImageFromFile(emojiFile)
        }


    }

    private fun picasso(imageView: ImageView, url: String){
        Picasso
            .get()
            .load(url)
            .into(imageView)
    }
}