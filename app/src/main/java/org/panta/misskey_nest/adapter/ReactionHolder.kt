package org.panta.misskey_nest.adapter

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.R
import org.panta.misskey_nest.emoji.CustomEmoji
import org.panta.misskey_nest.interfaces.ItemClickListener
import org.panta.misskey_nest.util.SVGParser

class ReactionHolder(itemView: View, private val  customEmoji: CustomEmoji) : RecyclerView.ViewHolder(itemView){

    private val reactionIcon = itemView.findViewById<ImageButton>(R.id.reaction_image_button)
    private val reactionStringIcon = itemView.findViewById<TextView>(R.id.reaction_type_string_view)
    private val reactionCount = itemView.findViewById<TextView>(R.id.reaction_count)
    private val reactionCountItem = itemView.findViewById<LinearLayout>(R.id.reaction_counter_view)
    private val reactionViewItem = itemView.findViewById<FrameLayout>(R.id.reaction_frame_item)

    var itemClickListener: ItemClickListener<String>? = null

    private val reactionImageMapping = hashMapOf("like" to R.drawable.ic_reaction_like ,
        "love" to R.drawable.ic_reaction_love ,
        "laugh" to R.drawable.ic_reaction_laugh,
        "hmm" to R.drawable.ic_reaction_hmm,
        "surprise" to R.drawable.ic_reaction_surprise ,
        "congrats" to R.drawable.ic_reaction_congrats,
        "angry" to R.drawable.ic_reaction_angry,
        "confused" to R.drawable.ic_reaction_confused,
        "rip" to R.drawable.ic_reaction_rip,
        "pudding" to R.drawable.ic_reaction_pudding)



    fun onBind(emoji: String, count: Int, isMyReaction: Boolean){
        val resourceId = reactionImageMapping[emoji]
        //val emojiFile = customReactionFileList?.firstOrNull{ it -> it.name.contains(emoji.replace(":", "")) }
        val emojiFile = customEmoji.getEmojisFile(emoji)

        if(resourceId == null && emojiFile == null){
            reactionStringIcon.visibility = View.VISIBLE
            reactionIcon.visibility = View.GONE
            reactionStringIcon.text = emoji
        }else if(resourceId != null){
            reactionStringIcon.visibility = View.GONE
            reactionIcon.visibility = View.VISIBLE
            reactionIcon.setImageResource(resourceId)
        }else if(emojiFile != null && emojiFile.name.endsWith(".svg")){
            reactionIcon.visibility = View.INVISIBLE
            reactionStringIcon.visibility = View.GONE
            GlobalScope.launch {
                try{
                    val bitmap = SVGParser(null).getBitmapFromFile(emojiFile, 50, 50)

                    Handler(Looper.getMainLooper()).post{
                        try{
                            reactionIcon.setImageBitmap(bitmap)
                            reactionIcon.visibility = View.VISIBLE
                        }catch(e: Exception){
                            Log.d("ReactionHolder", "error", e)
                        }
                    }
                }catch(e: Exception){
                    Log.d("ReactionHolder", "error", e)
                }

            }



            //Log.d("ReactionHolder", "SVGタイプの画像が来た")
        }else if(emojiFile != null && ! emojiFile.name.endsWith(".svg")){
            reactionStringIcon.visibility = View.GONE
            reactionIcon.visibility = View.VISIBLE
            Picasso
                .get()
                .load(emojiFile)
                .fit()
                .into(reactionIcon)
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