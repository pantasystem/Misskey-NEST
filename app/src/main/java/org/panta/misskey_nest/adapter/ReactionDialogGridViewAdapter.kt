package org.panta.misskey_nest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_reaction_dialogs_icon.view.*
import org.panta.misskey_nest.R
import org.panta.misskey_nest.constant.ReactionConstData


//タイムライン用のリアクションとリアクション選択時のGridViewを分別するか検討中
class ReactionDialogGridViewAdapter(private val context: Context, private val layoutId: Int, private val list: List<String>) : BaseAdapter(){


    private val reactionImageMapping = hashMapOf(
        ReactionConstData.LIKE to R.drawable.reaction_icon_like,
        ReactionConstData.LOVE to R.drawable.reaction_icon_love,
        ReactionConstData.HMM to R.drawable.reaction_icon_hmm,
        ReactionConstData.SURPRISE to R.drawable.reaction_icon_surprise,
        ReactionConstData.CONGRATS to R.drawable.reaction_icon_congrats,
        ReactionConstData.LAUGH to R.drawable.reaction_icon_laugh,


        ReactionConstData.ANGRY to R.drawable.reaction_icon_angry,
        ReactionConstData.CONFUSED to R.drawable.reaction_icon_confused,
        ReactionConstData.RIP to R.drawable.reaction_icon_rip,
        ReactionConstData.PUDDING to R.drawable.reaction_icon_pudding
    )

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

        imageView.setImageResource(reactionImageMapping[list[p0]]!!)
        return convertView
    }
}