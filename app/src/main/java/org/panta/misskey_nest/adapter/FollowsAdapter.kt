package org.panta.misskey_nest.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskey_nest.R
import org.panta.misskey_nest.constant.FollowFollowerType
import org.panta.misskey_nest.entity.FollowProperty
import org.panta.misskey_nest.entity.User
import org.panta.misskey_nest.interfaces.IOperationAdapter
import org.panta.misskey_nest.view_data.FollowViewData
import java.util.*

class FollowsAdapter(list: List<FollowViewData>, private val type: FollowFollowerType, private var followAdapterListener: FollowAdapterListener? = null)
    : RecyclerView.Adapter<FollowViewHolder>(), IOperationAdapter<FollowViewData>{

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

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FollowViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_follow_follower, p0, false)
        return FollowViewHolder(inflater, followAdapterListener)
    }

    override fun onBindViewHolder(p0: FollowViewHolder, p1: Int) {
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