package org.panta.misskeynest.adapter

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.emoji.CustomEmoji
import org.panta.misskeynest.entity.ReactionCountPair
import org.panta.misskeynest.interfaces.ItemClickListener
import java.io.File

class ReactionRecyclerAdapter(private val reactionList: List<ReactionCountPair>, private val myReactionType: String?, private val customEmoji: CustomEmoji)
    : RecyclerView.Adapter<org.panta.misskeynest.adapter.ReactionHolder>(){

    /*private val reactionList = reactionCountMap.map{
        it.key to it.value
    }*/

    var reactionItemClickListener: ItemClickListener<String>? = null



    override fun getItemCount(): Int {
        return reactionList.size
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): org.panta.misskeynest.adapter.ReactionHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_reaction_counter, p0, false)

        return org.panta.misskeynest.adapter.ReactionHolder(inflater, customEmoji)
    }

    override fun onBindViewHolder(p0: org.panta.misskeynest.adapter.ReactionHolder, position: Int) {
        val reaction = reactionList[position]
        val isMyReaction = reaction.reactionType == myReactionType
        //val drawable = Drawable.createFromPath("reaction_icon_${reaction.reactionType}.png")
        p0.onBind(reaction.reactionType, reaction.reactionCount, isMyReaction)

        p0.itemClickListener = reactionItemClickListener

    }
}