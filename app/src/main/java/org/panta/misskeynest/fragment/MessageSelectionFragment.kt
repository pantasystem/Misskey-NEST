package org.panta.misskeynest.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_message_selection.*
import org.panta.misskeynest.MessageActivity
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.MessageAdapter
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.filter.MessageFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.repository.remote.MessageRepository
import org.panta.misskeynest.usecase.interactor.HistoryUseCase
import org.panta.misskeynest.viewdata.MessageViewData

class MessageSelectionFragment : Fragment(){

    companion object{
        private const val CONNECTION_PROPERTY_KEY = "MessageSelectionFragmentConnectionPropertyKey"
        private const val IS_GROUP_KEY = "MessageSelectionFragmentIsGroupKey"
        fun getInstance(connectionProperty: ConnectionProperty, isGroup: Boolean): MessageSelectionFragment {
            val fragment = MessageSelectionFragment()
            val bundle = Bundle()
            bundle.putSerializable(CONNECTION_PROPERTY_KEY, connectionProperty)
            bundle.putBoolean(IS_GROUP_KEY, isGroup)
            fragment.arguments = bundle
            return fragment
        }

        fun getInstance(connectionProperty: ConnectionProperty): MessageSelectionFragment {
            return MessageSelectionFragment().apply{
                val bundle = Bundle()
                bundle.putSerializable(CONNECTION_PROPERTY_KEY, connectionProperty)
                this.arguments = bundle
            }
        }
    }
    private lateinit var mConnectionProperty: ConnectionProperty

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        mConnectionProperty = arguments?.getSerializable(CONNECTION_PROPERTY_KEY) !!as ConnectionProperty

        return LayoutInflater.from(context).inflate(R.layout.fragment_message_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val isGroup = arguments?.getBoolean(IS_GROUP_KEY)

        val historyUseCase = HistoryUseCase(MessageFilter(mConnectionProperty), MessageRepository(mConnectionProperty), errorListener)
        historyUseCase.getMixHistory {
            if( it != null ) setAdapter(it)
        }



    }

    private fun setAdapter(list: List<MessageViewData>){
        Handler(Looper.getMainLooper()).post{
            message_selection_list.adapter = MessageAdapter(list).apply{
                this.onItemClickListener = itemClickListener
            }
            message_selection_list.layoutManager = LinearLayoutManager(context)
        }
    }
    private val itemClickListener = object : ItemClickListener<MessageViewData>{
        override fun onClick(e: MessageViewData) {
            val intent = MessageActivity.getIntent(context!!, e)
            startActivity(intent)
        }
    }

    private val errorListener = object: ErrorCallBackListener{
        override fun callBack(e: Exception) {
            Log.w("MessageSelection", "error", e)
        }
    }
}