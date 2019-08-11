package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.viewdata.DriveViewData

class DriveAdapter(list: List<DriveViewData>) : RecyclerView.Adapter<AbsViewHolder<DriveViewData>>(), IOperationAdapter<DriveViewData>{

    companion object{
        private const val FOLDER = 0
        private const val FILE = 1
    }
    private val mArrayList = ArrayList<DriveViewData>()
    var itemClickListener: ItemClickListener<DriveViewData>? = null
    init{
        mArrayList.addAll(list)
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(mArrayList[position]){
            is DriveViewData.FolderViewData -> FOLDER
            is DriveViewData.FileViewData -> FILE
        }
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AbsViewHolder<DriveViewData> {
        return when(p1){
            FOLDER -> {
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_drive_folder, p0, false)
                FolderViewHolder(inflater)
            }
            FILE -> {
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_drive_file, p0, false)
                FileViewHolder(inflater)

            }
            else -> throw IllegalArgumentException("あり得ない値です")
        }

    }

    override fun onBindViewHolder(p0: AbsViewHolder<DriveViewData>, p1: Int) {
        //p0.itemView
        p0.itemView.setOnClickListener {
            itemClickListener?.onClick(mArrayList[p1])
        }
        val item = mArrayList[p1]
        p0.onBind(item)

    }

    override fun addAllFirst(list: List<DriveViewData>) {
        synchronized(mArrayList){
            mArrayList.addAll(0, list)
            notifyItemRangeInserted(0, list.size)

        }
    }

    override fun addAllLast(list: List<DriveViewData>) {
        synchronized(mArrayList){
            val startIndex = mArrayList.size
            mArrayList.addAll(list)
            notifyItemRangeInserted(startIndex, list.size)
        }
    }

    override fun getItem(item: DriveViewData): DriveViewData? {
        return getItem(item.id)
    }

    override fun getItem(index: Int): DriveViewData? {
        synchronized(mArrayList){
            return mArrayList[index]
        }
    }

    override fun getItem(id: String): DriveViewData? {
        synchronized(mArrayList){
            return mArrayList.firstOrNull {
                it.id == id
            }
        }
    }

    override fun removeItem(item: DriveViewData) {
        synchronized(mArrayList){
            val index = mArrayList.indexOf(item)
            mArrayList.remove(item)
            notifyItemRemoved(index)
        }
    }

    override fun updateItem(item: DriveViewData) {
        synchronized(mArrayList){
            for(n in 0 until mArrayList.size){
                val element = mArrayList[n]
                if(item.id == element.id){
                    mArrayList[n] = item
                    notifyItemChanged(n)
                }
            }
        }
    }
}