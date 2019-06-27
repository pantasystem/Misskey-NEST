package org.panta.misskeynest.adapter

import android.os.Handler
import android.os.Looper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.viewdata.MessageViewData

const val MESSAGE = 0
const val HISTORY = 1
class MessageAdapter(list: List<MessageViewData>, private val type: Int) : RecyclerView.Adapter<AbsMessageViewHolder>(), IOperationAdapter<MessageViewData> {

    companion object{
        private const val TYPE_HISTORY = 0
        private const val MESSAGE_OWN = 1
        private const val MESSAGE_PAIR = 2
    }

    private val mList = ArrayList<MessageViewData>().apply {
        addAll(list)
    }

    var onItemClickListener: ItemClickListener<MessageViewData>? = null
    var onUserClickListener: ItemClickListener<User>? = null

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when( type ){
            HISTORY -> {
                TYPE_HISTORY
            }
            MESSAGE ->{
                if( mList[position].isOwn ){
                    MESSAGE_OWN
                }else{
                    MESSAGE_PAIR
                }
            }
            else -> throw java.lang.IllegalArgumentException("コンストラクタ引数 typeはMESSAGE(0) HISTORY(1)しか許可されていません")
        }
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AbsMessageViewHolder {
        //val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_history, p0, false)
        return when(p1){
            TYPE_HISTORY ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_history, p0, false)
                val vh = MessageHistoryViewHolder(inflater)
                vh.itemClickListener = onItemClickListener
                vh
            }
            MESSAGE_OWN ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_self , p0, false)
                SelfMessageViewHolder(inflater)
            }
            MESSAGE_PAIR ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_partner, p0, false)
                PairMessageViewHolder(inflater).apply{
                    userClickListener = onUserClickListener
                }
            }
            else -> throw IllegalArgumentException("想定されていない値です")
        }
    }

    override fun onBindViewHolder(p0: AbsMessageViewHolder, p1: Int) {
        p0.onBind(mList[p1])
    }

    override fun addAllFirst(list: List<MessageViewData>) {
        synchronized(mList){
            mList.addAll(0, list)
            Handler(Looper.getMainLooper()).post{
                notifyItemRangeInserted(0, list.size)
            }
        }
    }

    override fun addAllLast(list: List<MessageViewData>) {
        val beforeIndex = mList.size
        synchronized(mList){
            mList.addAll(list)
        }
        Handler(Looper.getMainLooper()).post{
            notifyItemRangeChanged(mList.size, list.size)
                notifyItemRangeInserted(beforeIndex, list.size)

        }
    }

    override fun getItem(id: String): MessageViewData? {
        synchronized(mList){
            return try{
                mList.first {
                    it.id == id
                }
            }catch(e: Exception){
                Log.d("MessageAdapter", "error", e)
                null
            }
        }
    }

    override fun getItem(index: Int): MessageViewData? {
        synchronized(mList){
            return mList[index]
        }
    }

    override fun getItem(item: MessageViewData): MessageViewData? {
        return getItem(item.id)
    }

    override fun removeItem(item: MessageViewData) {
        TODO("気が向いたら実装する、めんどい( ´∀｀ )")
    }

    override fun updateItem(item: MessageViewData) {
        TODO("気が向いたら実装する、めんどい( ´∀｀ )")

    }
}
