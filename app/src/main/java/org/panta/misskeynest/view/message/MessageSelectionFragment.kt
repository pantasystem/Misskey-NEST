package org.panta.misskeynest.view.message

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.R
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.repository.remote.MessageRepository

class MessageSelectionFragment : Fragment(){

    companion object{
        private const val CONNECTION_PROPERTY_KEY = "MessageSelectionFragmentConnectionPropertyKey"
        private const val IS_GROUP_KEY = "MessageSelectionFragmentIsGroupKey"
        fun getInstance(connectionProperty: ConnectionProperty, isGroup: Boolean): MessageSelectionFragment{
            val fragment = MessageSelectionFragment()
            val bundle = Bundle()
            bundle.putSerializable(CONNECTION_PROPERTY_KEY, connectionProperty)
            bundle.putBoolean(IS_GROUP_KEY, isGroup)
            fragment.arguments = bundle
            return fragment
        }

        fun getInstance(connectionProperty: ConnectionProperty): MessageSelectionFragment{
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

        val isGroup = arguments?.getBoolean(IS_GROUP_KEY)
        val messageRepository = MessageRepository(mConnectionProperty)

        GlobalScope.launch {
            //val list = messageRepository.getHistory(isGroup)
        }
    }
}