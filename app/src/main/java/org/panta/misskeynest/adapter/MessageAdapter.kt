package org.panta.misskeynest.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.viewdata.MessageDataType
import org.panta.misskeynest.viewdata.MessageViewData

/**
 * 場合によってはMessageAdapterと統合する可能性有り
 * ただし現時点ではMessageAdapterを実装していないのでどんな影響を及ぼすかわからないので別にしている
 */
class MessageAdapter(private val list: List<MessageViewData>) : RecyclerView.Adapter<AbsMessageViewHolder>() {

    companion object{
        private const val TYPE_HISTORY = 0
        private const val MESSAGE_OWN = 1
        private const val MESSAGE_PAIR = 2
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when( list[position].messageType ){
            MessageDataType.HISTORY_USER, MessageDataType.HISTORY_GROUP -> {
                TYPE_HISTORY
            }
            MessageDataType.MESSAGE_USER, MessageDataType.MESSAGE_GROUP ->{
                if( list[position].isOwn ){
                    MESSAGE_OWN
                }else{
                    MESSAGE_PAIR
                }
            }
        }
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AbsMessageViewHolder {
        //val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_history, p0, false)
        return when(p1){
            TYPE_HISTORY ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_history, p0, false)
                MessageHistoryViewHolder(inflater)
            }
            MESSAGE_OWN ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_self , p0, false)
                SelfMessageViewHolder(inflater)
            }
            MESSAGE_PAIR ->{
                val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_message_partner, p0, false)
                PairMessageViewHolder(inflater)
            }
            else -> throw IllegalArgumentException("想定されていない値です")
        }
    }

    override fun onBindViewHolder(p0: AbsMessageViewHolder, p1: Int) {

        p0.onBind(list[p1])
    }
}
