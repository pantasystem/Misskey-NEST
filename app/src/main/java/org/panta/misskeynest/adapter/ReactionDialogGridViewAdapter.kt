package org.panta.misskeynest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_reaction_dialogs_icon.view.*
import org.panta.misskeynest.constant.ReactionConstData


//タイムライン用のリアクションとリアクション選択時のGridViewを分別するか検討中
class ReactionDialogGridViewAdapter(private val context: Context, private val layoutId: Int, private val list: List<String>) : BaseAdapter(){


    /*private val reactionImageMapping = hashMapOf(
        ReactionConstData.LIKE to R.drawable.ic_reaction_like,
        ReactionConstData.LOVE to R.drawable.ic_reaction_love,
        ReactionConstData.HMM to R.drawable.ic_reaction_hmm,
        ReactionConstData.SURPRISE to R.drawable.ic_reaction_surprise,
        ReactionConstData.CONGRATS to R.drawable.ic_reaction_congrats,
        ReactionConstData.LAUGH to R.drawable.ic_reaction_laugh,


        ReactionConstData.ANGRY to R.drawable.ic_reaction_angry,
        ReactionConstData.CONFUSED to R.drawable.ic_reaction_confused,
        ReactionConstData.RIP to R.drawable.ic_reaction_rip,
        ReactionConstData.PUDDING to R.drawable.ic_reaction_pudding
    )*/

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val imageView: ImageView
        var convertView = p1

        if(convertView == null){
            convertView = inflater.inflate(layoutId, null)
            imageView = convertView!!.reaction_image_view
            convertView.tag =imageView
        }else{
            imageView = convertView.tag as ImageView
        }

        imageView.setImageResource(ReactionConstData.getDefaultReactionIconMapping()[list[p0]]!!)
        return convertView
    }
}