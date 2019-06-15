package org.panta.misskeynest.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.viewdata.FollowViewData
import java.util.*

class FollowsAdapter(list: List<FollowViewData>, private val type: FollowFollowerType, private var followAdapterListener: org.panta.misskeynest.adapter.FollowsAdapter.FollowAdapterListener? = null)
    : RecyclerView.Adapter<org.panta.misskeynest.adapter.FollowViewHolder>(), IOperationAdapter<FollowViewData>{

    interface FollowAdapterListener{
        fun onUserClicked(user: User)
        fun onFollowButtonClicked(item: FollowViewData)
    }



    //listをキャストして扱うこともできるが元がLinkedListかもしれないのでArrayListとして作成する
    private val mArrayList = ArrayList<FollowViewData>(list)
    override fun getItemCount(): Int {
        synchronized(mArrayList){
            return mArrayList.size
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): org.panta.misskeynest.adapter.FollowViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_follow_follower, p0, false)
        return org.panta.misskeynest.adapter.FollowViewHolder(inflater, followAdapterListener)
    }

    override fun onBindViewHolder(p0: org.panta.misskeynest.adapter.FollowViewHolder, p1: Int) {
        val followProperty = mArrayList[p1]
        p0.bindItem(followProperty, type)
    }

    override fun addAllFirst(list: List<FollowViewData>) {
        synchronized(mArrayList){
            mArrayList.addAll(0, list)
        }
        Handler().post{
            notifyItemRangeInserted(0, list.size)
        }
    }

    override fun addAllLast(list: List<FollowViewData>) {
        val lastIndex = mArrayList.size
        synchronized(mArrayList){
            mArrayList.addAll(list)
        }
        Handler().post{
            //実験段階不具合の可能性有り
            notifyItemRangeInserted(lastIndex, list.size)
        }
    }

    override fun getItem(index: Int): FollowViewData {
        return mArrayList[index]
    }

    override fun getItem(item: FollowViewData): FollowViewData {
        synchronized(mArrayList){
            return mArrayList.first{ it.id == item.id }
        }
    }

    override fun removeItem(item: FollowViewData) {
        synchronized(mArrayList){
            val index = mArrayList.indexOf(item)
            mArrayList.remove(item)

            Handler().post{
                notifyItemRemoved(index)
            }
        }
    }

    override fun updateItem(item: FollowViewData) {
        synchronized(mArrayList){
            for(n in 0.until(mArrayList.size)){
                if(mArrayList[n].id == item.id){
                    mArrayList[n] = item
                    notifyItemChanged(n)
                }

            }
        }
    }
}