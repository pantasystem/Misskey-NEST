package org.panta.misskeynest.presenter

import android.util.Log
import org.panta.misskeynest.contract.MessageContract
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.CallBackListener
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.repository.IMessageRepository
import org.panta.misskeynest.usecase.IMessageChannelUseCase
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.viewdata.MessageViewData

class MessagePresenter(private val mView: MessageContract.View,
                       private val mRepository: IMessageRepository,
                       mPagingRepository: IItemRepository<MessageProperty>,
                       mFilter: IItemFilter<MessageProperty, MessageViewData>,
                       private val mMessageChannelUseCase: IMessageChannelUseCase
                       ) : MessageContract.Presenter{

    private val errorListener = object : ErrorCallBackListener{
        override fun callBack(e: Exception) {
            Log.d("", "error", e)
        }
    }

    private val mPagingController = PagingController<MessageProperty, MessageViewData>(mPagingRepository, errorListener ,mFilter)


    override fun getNewMessage() {
        mPagingController.getNewItems {
            mView.showNewMessage(it.asReversed())
        }
    }

    override fun getOldMessage() {
        mPagingController.getOldItems{
            mView.showOldMessage(it.asReversed())
        }
    }

    override fun start() {
        mPagingController.init {
            mView.showMessage(it.asReversed())
        }
        mMessageChannelUseCase.start()
        mMessageChannelUseCase.messageReceivedListener = mMessageListener
    }

    private val mMessageListener = object : CallBackListener<List<MessageViewData>>{
        override fun callBack(e: List<MessageViewData>) {
            mView.showRecievedMessage(e)
        }
    }

}