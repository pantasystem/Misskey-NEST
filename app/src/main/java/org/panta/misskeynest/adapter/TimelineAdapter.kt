package org.panta.misskeynest.adapter


import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.emoji.CustomEmoji
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.interfaces.IUserClickListener
import org.panta.misskeynest.usecase.NoteAdjustment
import org.panta.misskeynest.view_data.NoteViewData

class TimelineAdapter(private val context: Context, notesList: List<NoteViewData>) : RecyclerView.Adapter<NoteViewHolder>(), IOperationAdapter<NoteViewData>{

    private val mArrayList = ArrayList<NoteViewData>(notesList)
    private var noteClickListener: INoteClickListener? = null
    private var userClickListener: IUserClickListener? = null

    //var reactionIconFileList: List<File>? = null

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return NoteViewHolder(inflater, lm, CustomEmoji(p0.context))

    }

    override fun onBindViewHolder(viewHolder: NoteViewHolder, p1: Int) {
        val viewData = mArrayList[p1]
        //viewHolder.reactionIconFileList = emojiFileList

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

    override fun getItem(index: Int): NoteViewData?{
        synchronized(mArrayList){
            return try{
                mArrayList[index]
            }catch(e: Exception){
                Log.d("TimelineAdapter", "インデックスがオーバーしてる！！", e)
                null
            }
        }
    }

    override fun getItem(item: NoteViewData): NoteViewData? {
        synchronized(mArrayList){
            return try{
                mArrayList.first { it.id == item.id }

            }catch(e: Exception){
                Log.d("TimelineAdapter", "インデックスがオーバーしてる！！", e)
                null
            }
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

    fun addNoteClickListener(listener: INoteClickListener){
        this.noteClickListener = listener
    }

    fun addUserClickListener(listener: IUserClickListener){
        this.userClickListener = listener
    }


}
