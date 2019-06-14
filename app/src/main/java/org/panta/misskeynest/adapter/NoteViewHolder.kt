package org.panta.misskeynest.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.item_note.view.*
import org.panta.misskeynest.interfaces.ITimeFormat
import org.panta.misskeynest.util.ElapsedTimeFormatter

open class NoteViewHolder(itemView: View, private val mLinearLayoutManager: LinearLayoutManager?,
                          override val mTimeFormatter: ITimeFormat = ElapsedTimeFormatter()) : AbsNoteViewHolder(itemView){


    override val timelineItem: RelativeLayout = itemView.base_layout
    override val whoReactionUserLink: Button = itemView.who_reaction_user_link
    override val userIcon: ImageView = itemView.user_icon
    override val userName: TextView = itemView.user_name
    override val userId: TextView = itemView.user_id
    override val noteText: TextView = itemView.note_text
    override val elapsedTime: TextView = itemView.elapsed_time

    override val imageViewList: List<ImageView> = listOf(itemView.image_1, itemView.image_2, itemView.image_3, itemView.image_4)

    override val subNote : RelativeLayout = itemView.sub_note
    override val subUserIcon: ImageView = itemView.sub_user_icon
    override val subUserName: TextView = itemView.sub_user_name
    override val subUserId: TextView = itemView.sub_user_id
    override val subNoteText: TextView = itemView.sub_text

    override val reactionView: RecyclerView = itemView.reaction_view

    override val replyButton: ImageButton = itemView.reply_button
    override val replyCount: TextView = itemView.reply_count
    override val reNoteButton: ImageButton = itemView.re_note_button
    override val reNoteCount: TextView = itemView.re_note_count
    override val reactionButton: ImageButton = itemView.reaction_button
    override val descriptionButton: ImageButton = itemView.description_button

    override val showThreadButton: Button = itemView.show_thread_button

    override val mediaPlayButton: Button = itemView.media_play_button





}