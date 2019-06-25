package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.ReactionConstData
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.usecase.interactor.InjectReaction
import org.panta.misskeynest.util.getEmojiPathFromName

class ReactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val reactionIcon = itemView.findViewById<ImageButton>(R.id.reaction_image_button)
    private val reactionStringIcon = itemView.findViewById<TextView>(R.id.reaction_type_string_view)
    private val reactionCount = itemView.findViewById<TextView>(R.id.reaction_count)
    private val reactionCountItem = itemView.findViewById<LinearLayout>(R.id.reaction_counter_view)
    private val reactionViewItem = itemView.findViewById<FrameLayout>(R.id.reaction_frame_item)
    private val view = itemView

    var itemClickListener: ItemClickListener<String>? = null


    private val reactionImageMapping = ReactionConstData.getDefaultReactionIconMapping()


    fun onBind(emoji: String, count: Int, isMyReaction: Boolean){
        val resourceId = reactionImageMapping[emoji]
        val emojiFile = getEmojiPathFromName(itemView.context, emoji.replace(":", ""))

        val showReaction = InjectReaction(reactionIcon, reactionStringIcon)

        if(resourceId == null && emojiFile == null){
            //全てに当てはまらない場合
            showReaction.setTextReaction(emoji)
        }else if(resourceId != null){
            //定数画像に含まれる場合
            showReaction.setReactionFromResource(resourceId)
        }else if(emojiFile != null){
            showReaction.setImageFromFile(emojiFile)
        }



        //リアクションのカウントを設定
        reactionCount.text = count.toString()

        if(isMyReaction){
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