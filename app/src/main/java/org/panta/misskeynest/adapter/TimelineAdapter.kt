package org.panta.misskeynest.adapter


import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.interfaces.IUserClickListener
import org.panta.misskeynest.viewdata.NoteViewData
const val REACTION_UPDATED = 0
const val NOTE_UPDATED = 1

class TimelineAdapter(notesList: List<NoteViewData>) : AbsTimelineAdapter<NoteViewHolder>(), IOperationAdapter<NoteViewData>{

    override val mArrayList = ArrayList<NoteViewData>(notesList)
    private var noteClickListener: INoteClickListener? = null
    private var userClickListener: IUserClickListener? = null

    //var reactionIconFileList: List<File>? = null

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
        val lm = LinearLayoutManager(p0.context, LinearLayoutManager.HORIZONTAL, false)
        return NoteViewHolder(inflater, lm)

    }

    override fun onBindViewHolder(viewHolder: NoteViewHolder, p1: Int) {
        val viewData = mArrayList[p1]
        //viewHolder.reactionIconFileList = emojiFileList

       //リアクションをセットしている
        viewHolder.onBind(viewData)

        viewHolder.addOnItemClickListener(noteClickListener)
        viewHolder.addOnUserClickListener(userClickListener)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, payloads: MutableList<Any>) {
        //super.onBindViewHolder(holder, position, payloads)
        if( payloads.any() ){
            val payload = payloads[0] as Int
            if(payload == NOTE_UPDATED){

            }else if( payload == REACTION_UPDATED ){
                holder.setReactionCount(mArrayList[position])
            }
        }
        onBindViewHolder( holder, position )
    }




    fun addNoteClickListener(listener: INoteClickListener){
        this.noteClickListener = listener
    }

    fun addUserClickListener(listener: IUserClickListener){
        this.userClickListener = listener
    }


}
