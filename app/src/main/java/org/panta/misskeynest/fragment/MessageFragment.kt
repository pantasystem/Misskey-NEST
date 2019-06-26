package org.panta.misskeynest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskeynest.R
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.filter.MessageFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.repository.remote.MessagePagingRepository
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.viewdata.MessageViewData

class MessageFragment : Fragment(){

    companion object{
        private const val MESSAGE_DATA_KEY = "MessageFragmentMessageViewDataKey"
        private const val CONNECTION_PROPERTY_KEY = "MessageFragmentConnectionProperty"

        fun getInstance(connectionProperty: ConnectionProperty, messageViewData: MessageViewData): MessageFragment{
            return MessageFragment().apply{
                val bundle = Bundle()
                bundle.putSerializable(MESSAGE_DATA_KEY, messageViewData)
                bundle.putSerializable(CONNECTION_PROPERTY_KEY, connectionProperty)
                arguments = bundle
            }
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getSerializable( MESSAGE_DATA_KEY ) as MessageViewData
        val connectionProperty = arguments?.getSerializable( CONNECTION_PROPERTY_KEY ) as ConnectionProperty
        val group = message.message.group
        val recipient = message.message.recipient

        val repository = MessagePagingRepository(groupId = group?.id, userId = recipient?.id, connectionProperty = connectionProperty)
        val filter = MessageFilter(connectionProperty)
        val paging = PagingController(repository , errorListener, filter)


    }

    val errorListener = object : ErrorCallBackListener{
        override fun callBack(e: Exception) {
            Log.w("", "error", e)
        }
    }

}