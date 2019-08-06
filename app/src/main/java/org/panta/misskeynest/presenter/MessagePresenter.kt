package org.panta.misskeynest.presenter

import android.util.Log
import org.panta.misskeynest.contract.MessageContract
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.CallBackListener
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.usecase.IMessageChannelUseCase
import org.panta.misskeynest.usecase.IMessageUseCase
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.viewdata.MessageViewData
import java.io.File

class MessagePresenter(private val mView: MessageContract.View,
                       private val mMessageUseCase: IMessageUseCase,
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

    private var mFileProperty: FileProperty? = null

    private val mConnection = OkHttpConnection()

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

    override fun sendMessage(text: String?) {
        if(text == null){
            mView.showFailureSendMessage()
            return
        }
        mMessageUseCase.sendMessage(text){
            if(it){
                mView.showSuccessSendMessage()
            }else{
                mView.showFailureSendMessage()
            }
        }
    }

    override fun uploadFile(file: File) {
        //FileをPOSTする
    }

    override fun setFile(file: FileProperty) {
        mFileProperty = file
    }

    private val mMessageListener = object : CallBackListener<List<MessageViewData>>{
        override fun callBack(e: List<MessageViewData>) {
            mView.showRecievedMessage(e)
        }
    }

}