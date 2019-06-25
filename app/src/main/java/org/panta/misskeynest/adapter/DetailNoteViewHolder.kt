package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.item_detailed_note.view.*
import org.panta.misskeynest.interfaces.ITimeFormat
import org.panta.misskeynest.util.ElapsedTimeFormatter

class DetailNoteViewHolder(itemView: View) : AbsNoteViewHolder(itemView){

    override val timelineItem: RelativeLayout = itemView.item_view
    override val whoReactionUserLink: Button = itemView.who_reaction_user_link
    override val userIcon: ImageView = itemView.user_icon
    override val userName: TextView = itemView.user_name
    override val userId: TextView = itemView.user_name
    override val noteText: TextView = itemView.note_text

    override val subNote: RelativeLayout = itemView.sub_note
    override val subUserIcon: ImageView = itemView.sub_user_icon
    override val subUserName: TextView = itemView.sub_user_name
    override val subUserId: TextView = itemView.sub_user_id

    override val reactionView: RecyclerView = itemView.reaction_view

    override val replyButton: ImageButton = itemView.reply_button
    override val reNoteButton: ImageButton = itemView.re_note_button
    override val reactionButton: ImageButton = itemView.reaction_button
    override val descriptionButton: ImageButton = itemView.description_button

    //しっかりと実装する
    override val mediaPlayButton: Button = Button(itemView.context)

    override val reNoteCount: TextView = itemView.re_note_count
    override val replyCount: TextView = itemView.reply_count
    override val showThreadButton: Button = Button(itemView.context).apply { visibility = View.GONE }
    override val elapsedTime: TextView = itemView.elapsed_time
    override val subNoteText: TextView = itemView.sub_text
    override val imageViewList: List<ImageView> = listOf(itemView.image_1, itemView.image_2, itemView.image_3, itemView.image_4)
    override val mTimeFormatter: ITimeFormat = ElapsedTimeFormatter()

}