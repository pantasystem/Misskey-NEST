package org.panta.misskeynest.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_message.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.MESSAGE
import org.panta.misskeynest.adapter.MessageAdapter
import org.panta.misskeynest.contract.MessageContract
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.filter.MessageFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.presenter.MessagePresenter
import org.panta.misskeynest.repository.remote.MessagePagingRepository
import org.panta.misskeynest.repository.remote.MessageRepository
import org.panta.misskeynest.usecase.interactor.MessageChannelUseCase
import org.panta.misskeynest.viewdata.MessageViewData

class MessageFragment : Fragment(), MessageContract.View {

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

    override var mPresenter: MessageContract.Presenter? = null

    private var mAdapter: MessageAdapter? = null

    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getSerializable( MESSAGE_DATA_KEY ) as MessageViewData
        val group = message.message.group
        val connectionProperty = arguments?.getSerializable( CONNECTION_PROPERTY_KEY ) as ConnectionProperty

        //初期ID導出ロジック
        val myId = connectionProperty.userPrimaryId
        val baseUser = if(message.message.recipient?.id == myId){
            message.message.user
        }else{
            message.message.recipient
        }

        val pagingRepository = MessagePagingRepository(groupId = group?.id, userId = baseUser?.id, connectionProperty = connectionProperty)
        val messageRepository = MessageRepository(connectionProperty)

        val filter = MessageFilter(connectionProperty)

        val messageChannelUseCase = MessageChannelUseCase(connectionProperty, filter, message)

        mPresenter = MessagePresenter(this,  messageRepository, pagingRepository, filter, messageChannelUseCase)

        mPresenter?.start()

        mLayoutManager = LinearLayoutManager(context)

        messages_view.addOnScrollListener(scrollListener)

        message_send_button.setOnClickListener{
            mPresenter?.sendMessage(message_input_box.text.toString())
        }

        message_option_button.setOnClickListener{

        }

    }

    override fun showMessage(list: List<MessageViewData>) {
        Handler(Looper.getMainLooper()).post{
            val adapter = MessageAdapter(list, MESSAGE)
            messages_view.layoutManager = mLayoutManager
            messages_view.adapter = adapter

            Log.d("", "Messageを取得: $list")
            mAdapter = adapter
            messages_view.scrollToPosition(list.size - 1)

        }
    }

    override fun showNewMessage(list: List<MessageViewData>) {
        Handler(Looper.getMainLooper()).post{
            mAdapter?.addAllLast(list)
        }
    }

    override fun showOldMessage(list: List<MessageViewData>) {
        Handler(Looper.getMainLooper()).post{
            mAdapter?.addAllFirst(list)
        }
    }

    override fun showRecievedMessage(list: List<MessageViewData>) {
        Handler(Looper.getMainLooper()).post{
            val lastPosition = mLayoutManager.findLastVisibleItemPosition()
            val itemCount = mLayoutManager.itemCount - 1
            val isLast =itemCount == lastPosition


                mAdapter?.addAllLast(list)

            Log.d("", "lastPosition $lastPosition, itemCount: $itemCount")
            Log.d("", "MessageViewData $list")

            if( isLast){
                mLayoutManager.scrollToPosition( itemCount + list.size)

            }
        }
    }

    override fun showFileManager() {

    }

    override fun onUploadFile(file: FileProperty?) {

    }



    private val errorListener = object : ErrorCallBackListener{
        override fun callBack(e: Exception) {
            Log.w("", "error", e)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val first =mLayoutManager.findFirstVisibleItemPosition()
            Log.d("Scrolled", "first :$first")

            if( first == 0 ){
                mPresenter?.getOldMessage()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        }
    }
}