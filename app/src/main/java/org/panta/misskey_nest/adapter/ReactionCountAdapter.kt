package org.panta.misskey_nest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.item_reaction_counter.view.*
import org.panta.misskey_nest.R
import org.panta.misskey_nest.entity.ReactionCountPair



@Deprecated("ReactionRecyclerAdapterを使用すること!!") class ReactionCountAdapter(private val context: Context, private val layoutId: Int, private val reactionCountPairList: List<ReactionCountPair>) : BaseAdapter(){

    private data class ViewHolder(val reactionIconView: ImageButton, val reactionCount: TextView)
    private val inflater = LayoutInflater.from(context)

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

    override fun getCount(): Int {
        return reactionCountPairList.size
    }

    override fun getItem(p0: Int): Any {
        return reactionCountPairList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        val holder: ViewHolder
        if(convertView == null){
            convertView = inflater.inflate(layoutId, null)
            holder = ViewHolder(
                convertView!!.reaction_image_button,
                convertView.reaction_count
            )
            convertView.tag = holder
        }else{
            holder = convertView.tag as ViewHolder
        }

        val icon = reactionImageMapping[reactionCountPairList[position].reactionType]
        if(icon != null){
            holder.reactionIconView.setImageResource(icon)
        }else{
            holder.reactionIconView.setImageResource(R.drawable.human_icon)
        }
        holder.reactionCount.text = reactionCountPairList[position].reactionCount.toString()

        return convertView

    }
}