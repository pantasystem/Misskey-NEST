package org.panta.misskey_nest.adapter


import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskey_nest.R
import org.panta.misskey_nest.interfaces.IOperationAdapter
import org.panta.misskey_nest.interfaces.NoteClickListener
import org.panta.misskey_nest.interfaces.UserClickListener
import org.panta.misskey_nest.usecase.NoteAdjustment
import org.panta.misskey_nest.view_data.NoteViewData
import java.io.File

class TimelineAdapter(private val context: Context, notesList: List<NoteViewData>) : RecyclerView.Adapter<NoteViewHolder>(), IOperationAdapter<NoteViewData>{

    private val mArrayList = ArrayList<NoteViewData>(notesList)
    private var noteClickListener: NoteClickListener? = null
    private var userClickListener: UserClickListener? = null

    var reactionIconFileList: List<File>? = null

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return NoteViewHolder(inflater, lm)

    }

    override fun onBindViewHolder(viewHolder: NoteViewHolder, p1: Int) {
        val viewData = mArrayList[p1]
        viewHolder.reactionIconFileList = reactionIconFileList

       //リアクションをセットしている
        if(viewData.reactionCountPairList.isNotEmpty()){

        }else{
            viewHolder.invisibleReactionCount()
        }
        when{
            viewData.type == NoteAdjustment.NoteType.REPLY -> {
                viewHolder.setReply(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.REPLY_TO ->{
                viewHolder.setReplyTo(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.NOTE -> {
                //これはNote
                viewHolder.setNote(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.RE_NOTE -> {
                //これはリノート
                viewHolder.setReNote(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.QUOTE_RE_NOTE -> {
                viewHolder.setQuoteReNote(viewData)
            }
        }

        viewHolder.addOnItemClickListener(noteClickListener)
        viewHolder.addOnUserClickListener(userClickListener)

    }


    override fun addAllFirst(list: List<NoteViewData>){
        synchronized(mArrayList){
            mArrayList.addAll(0, list)
        }
        Handler().post{
            notifyItemRangeInserted(0, list.size)
        }
    }

    override fun addAllLast(list: List<NoteViewData>){
        val lastIndex = mArrayList.size
        synchronized(mArrayList){
            mArrayList.addAll(list)
        }
        Handler().post{
            notifyItemRangeInserted(lastIndex, list.size)
        }
    }

    override fun getItem(index: Int): NoteViewData{
        synchronized(mArrayList){
            return mArrayList[index]
        }
    }

    override fun getItem(item: NoteViewData): NoteViewData {
        synchronized(mArrayList){
            return mArrayList.first { it.id == item.id }
        }
    }

    override fun updateItem(item: NoteViewData){
       // var index = -1
        synchronized(mArrayList){

            for(n in 0.until(mArrayList.size)){
                val beforeData = mArrayList[n]
                if(beforeData.toShowNote.id == item.toShowNote.id){
                    mArrayList[n] = beforeData.copy(toShowNote = item.toShowNote, reactionCountPairList = item.reactionCountPairList)
                    Handler().post{
                        notifyItemChanged(n)
                    }
                }
            }

        }

    }


    override fun removeItem(item: NoteViewData){
        synchronized(mArrayList){
            val index = mArrayList.indexOf(item)
            mArrayList.remove(item)

            Handler().post{
                notifyItemRemoved(index)
            }
        }
    }

    fun addNoteClickListener(listener: NoteClickListener){
        this.noteClickListener = listener
    }

    fun addUserClickListener(listener: UserClickListener){
        this.userClickListener = listener
    }


}
