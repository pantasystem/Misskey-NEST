package org.panta.misskey_nest.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.R
import org.panta.misskey_nest.emoji.CustomEmoji
import org.panta.misskey_nest.interfaces.ItemClickListener
import java.io.File
import kotlin.contracts.contract

class ReactionHolder(itemView: View, private val customReactionFileList: List<File>? = null) : RecyclerView.ViewHolder(itemView){

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


    /*fun showReaction(count: String, emoji: String, isHasMyReaction: Boolean = false){
        val resourceId= reactionImageMapping[emoji]
        if(resourceId == null){
            reactionIcon.visibility = View.GONE
            reactionStringIcon.visibility = View.VISIBLE
            if(emoji.startsWith(":") && emoji.endsWith(":")){
                val customEmoji = emoji.replace(":","")
                Log.d("ReactionHolder", "カスタム絵文字のご登場だ $customEmoji")
                val customEmojiFile = customReactionFileList?.firstOrNull{
                    it.name.contains(customEmoji)
                }
                if(customEmojiFile != null){
                    Picasso
                        .get()
                        .load(customEmojiFile)
                        .fit()
                        .into(reactionIcon)


                    reactionIcon.visibility = View.VISIBLE
                    reactionStringIcon.visibility = View.GONE
                }else{
                    Log.d("ReactionHolder", "カスタム絵文字がNull")
                    Log.d("ReactionHolder", customReactionFileList.toString())
                    reactionStringIcon.text = emoji
                }
            }else{
                reactionStringIcon.text = emoji
            }
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

    }*/

    fun onBind(emoji: String, count: Int, isMyReaction: Boolean){
        val resourceId = reactionImageMapping[emoji]
        val emojiFile = customReactionFileList?.firstOrNull{ it -> it.name.contains(emoji.replace(":", "")) }

        if(resourceId == null && emojiFile == null){
            reactionStringIcon.visibility = View.VISIBLE
            reactionIcon.visibility = View.GONE
            reactionStringIcon.text = emoji
        }else if(resourceId != null){
            reactionStringIcon.visibility = View.GONE
            reactionIcon.visibility = View.VISIBLE
            Picasso
                .get()
                .load(resourceId)
                .fit()
                .into(reactionIcon)
        }else if(emojiFile != null && emojiFile.name.endsWith(".svg")){
            reactionIcon.visibility = View.INVISIBLE
            GlobalScope.apply{
                try{
                    launch {
                        launch(Dispatchers.Main){
                            reactionStringIcon.visibility = View.GONE
                        }
                        val bitmap = CustomEmoji.getBitmapFromSVG(emojiFile, 50, 50)
                        launch(Dispatchers.Main){
                            try{
                                reactionIcon.setImageBitmap(bitmap)
                                reactionIcon.visibility = View.VISIBLE
                            }catch(e: Exception){
                                Log.d("ReactionHolder", "error", e)
                            }

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