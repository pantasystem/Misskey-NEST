package org.panta.misskeynest.usecase.interactor

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.repository.remote.MessageRepository
import org.panta.misskeynest.usecase.IHistoryUseCase
import org.panta.misskeynest.viewdata.MessageViewData

class HistoryUseCase(override val mFilter: IItemFilter<MessageProperty, MessageViewData>,
                     private val mRepository: MessageRepository,
                     private val mErrorListener: ErrorCallBackListener
) : IHistoryUseCase{

    override fun getGroupHistory(callBack: (List<MessageViewData>?) -> Unit) {
        getHistory(true, callBack)
    }

    override fun getHistory(callBack: (List<MessageViewData>?) -> Unit) {
        getHistory(false, callBack)
    }

    override fun getMixHistory(callBack: (List<MessageViewData>?) -> Unit) {
        GlobalScope.launch{
            try{
                val groupHistoryList =asyncGetHistory(true)
                val historyList = asyncGetHistory(false)

                val groupHistoryViewData =
                    if( groupHistoryList != null) mFilter.filter(groupHistoryList) else null

                val historyViewData =
                    if( historyList != null ) mFilter.filter( historyList ) else null

                val viewDataList = ArrayList<MessageViewData>()
                if( groupHistoryViewData != null ) viewDataList.addAll(groupHistoryViewData)
                if( historyViewData != null ) viewDataList.addAll(historyViewData)

                callBack( viewDataList )
            }catch(e: Exception){
                callBack(null)
                mErrorListener.callBack(e)
            }


        }
    }

    private fun getHistory(isGroup: Boolean, callBack: (List<MessageViewData>?) -> Unit){
        GlobalScope.launch {
            try{
                val history = mRepository.getHistory(isGroup)
                if( history == null ){
                    callBack(null)
                }else{

                    val historyViewData = mFilter.filter(history)
                    callBack(historyViewData)
                }
            }catch(e: Exception){
                callBack(null)
                mErrorListener.callBack(e)
            }

        }
    }


    private suspend fun asyncGetHistory(isGroup: Boolean): List<MessageProperty>? {
        return withContext(Dispatchers.Default) {
            try {
                mRepository.getHistory(isGroup)
            } catch (e: Exception) {
                Log.w("", "getHistory中にエラー", e)
                null
            }
        }
    }


}