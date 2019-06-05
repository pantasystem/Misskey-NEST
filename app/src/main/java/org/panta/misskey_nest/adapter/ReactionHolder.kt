package org.panta.misskey_nest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.panta.misskey_nest.R
import org.panta.misskey_nest.interfaces.ItemClickListener
import kotlin.contracts.contract

class ReactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val reactionIcon = itemView.findViewById<ImageButton>(R.id.reaction_image_button)
    private val reactionStringIcon = itemView.findViewById<TextView>(R.id.reaction_type_string_view)
    private val reactionCount = itemView.findViewById<TextView>(R.id.reaction_count)
    private val reactionCountItem = itemView.findViewById<LinearLayout>(R.id.reaction_counter_view)
    private val reactionViewItem = itemView.findViewById<FrameLayout>(R.id.reaction_frame_item)

    var itemClickListener: ItemClickListener<String>? = null

    private val reactionImageMapping = hashMapOf("like" to R.drawable.reaction_icon_like ,
        "love" to R.drawable.reaction_icon_love ,
        "laugh" to R.drawable.reaction_icon_laugh,
        "hmm" to R.drawable.reaction_icon_hmm,
        "surprise" to R.drawable.reaction_icon_surprise ,
        "congrats" to R.drawable.reaction_icon_congrats,
        "angry" to R.drawable.reaction_icon_angry,
        "confused" to R.drawable.reaction_icon_confused,
        "rip" to R.drawable.reaction_icon_rip,
        "pudding" to R.drawable.reaction_icon_pudding)


    fun showReaction(count: String, emoji: String, isHasMyReaction: Boolean = false){
        val resourceId= reactionImageMapping[emoji]
        if(resourceId == null){
            reactionIcon.visibility = View.GONE
            reactionStringIcon.visibility = View.VISIBLE
            reactionStringIcon.text = emoji
        }else{
            reactionIcon.visibility = View.VISIBLE
            reactionStringIcon.visibility = View.GONE
            Picasso
                .get()
                .load(resourceId)
                .into(reactionIcon)
        }
        reactionCount.text = count
        if(isHasMyReaction){
            reactionCountItem.setBackgroundResource(R.drawable.shape_selected_reaction_background)
        }else{
            reactionCountItem.setBackgroundResource(R.drawable.shape_normal_reaction_background)

        }

        val listener = View.OnClickListener {
            itemClickListener?.onClick(emoji)
        }
        reactionIcon.setOnClickListener(listener)
        reactionStringIcon.setOnClickListener(listener)
        reactionCount.setOnClickListener(listener)
        reactionCountItem.setOnClickListener(listener)
        reactionViewItem.setOnClickListener(listener)

    }

}