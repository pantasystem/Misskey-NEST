package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_reaction_dialogs_icon.view.*
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.ReactionConstData
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.util.InjectionImage
import org.panta.misskeynest.util.SVGParser
import org.panta.misskeynest.util.convertDp2Px
import java.io.File

data class ReactionProperty(val name: String, val file: File?)

class ReactionViewerHolder(itemView: View, private val itemClickListener: ItemClickListener<String>) : RecyclerView.ViewHolder(itemView){
    private val mReactionIcon = itemView.reaction_image_view
    private val mReactionName = itemView.reaction_name

    private val view = itemView

    fun onBind(property: ReactionProperty){
        if( property.file == null ){
            val drawableId = ReactionConstData.getDefaultReactionIconMapping()[property.name]
            InjectionImage().injectionImage(drawableId!!, mReactionIcon, false)
        }else if(property.name.split(".").lastOrNull() == "svg"){
            val size = convertDp2Px(30F, mReactionIcon.context).toInt()
            val bitmap = SVGParser().getBitmapFromFile(property.file, size, size)

            //TODO Bitmapで扱うとメモリリースる可能性がある
            //TODO 他ライブラリを使うか必ずrecycleする
            //TODO　キャッシュができておらず不効率なのでキャッシュする
            mReactionIcon.setImageBitmap(bitmap)
        }else{
            InjectionImage().injectionImage( property.file, mReactionIcon, false)
        }
        mReactionName.text = property.name
        view.setOnClickListener {
            itemClickListener.onClick(property.name)
        }

    }
}
class ReactionViewerAdapter(reactionFile: List<File>,private val  itemClickListener: ItemClickListener<String>) : RecyclerView.Adapter<ReactionViewerHolder>(){


    private val reactionList = ArrayList<ReactionProperty>()

    init{

        ReactionConstData.getAllConstReactionList().forEach {
            reactionList.add(ReactionProperty(it, null))
        }
        reactionFile.forEach{
            val p = ReactionProperty(it.name.split(".")[0], it)
            reactionList.add(p)
        }
    }
    override fun getItemCount(): Int {
        return reactionList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReactionViewerHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_reaction_dialogs_icon, p0, false)
        return ReactionViewerHolder(inflater, itemClickListener)
    }

    override fun onBindViewHolder(p0: ReactionViewerHolder, p1: Int) {
        val property = reactionList[p1]
        p0.onBind(property)

    }

}