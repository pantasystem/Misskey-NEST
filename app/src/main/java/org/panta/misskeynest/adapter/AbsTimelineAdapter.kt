package org.panta.misskeynest.adapter


import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.util.Log
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.viewdata.NoteViewData


abstract class AbsTimelineAdapter<E: AbsNoteViewHolder> : RecyclerView.Adapter<E>(), IOperationAdapter<NoteViewData>{
    abstract val mArrayList: ArrayList<NoteViewData>

    //var reactionIconFileList: List<File>? = null


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
        return getItem(item.id)
    }

    override fun getItem(id: String): NoteViewData? {
        synchronized(mArrayList){
            return try{
                mArrayList.first { it.id == id }

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
                        notifyItemChanged(n, REACTION_UPDATED)
                    }
                }
            }

        }

    }


    override fun removeItem(item: NoteViewData){
        synchronized(mArrayList){
            //val index = mArrayList.indexOf(item)
            //mArrayList.remove(item)
            val iterator = mArrayList.iterator()
            while(iterator.hasNext()){
                val data = iterator.next()
                val index = mArrayList.indexOf(data)
                if( data.id == item.id ){
                    iterator.remove()
                    Handler(Looper.getMainLooper()).post{
                        notifyItemRemoved(index)
                    }
                }
            }



        }
    }



}
