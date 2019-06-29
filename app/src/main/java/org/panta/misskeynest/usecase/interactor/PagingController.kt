package org.panta.misskeynest.usecase.interactor

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.ID
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.usecase.IPaging

class PagingController<I: Any ,E: ID>(
    private val mRepository: IItemRepository<I>,
    private val errorCallBackListener: ErrorCallBackListener,
    private val mFilter: IItemFilter<I, E>
): IPaging<E> {
    private var latestId: String? = null
    private var oldestId: String? = null

    private var requestOldestFlag: String? = null

    override fun getNewItems(callBack:(List<E>)->Unit){
        if(latestId == null){
            init(null, callBack)
            return
        }
        GlobalScope.launch{
            val list = mRepository.getItemsUseSinceId(latestId!!)
            if(list == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@launch
            }
            val filteredList = mFilter.filter(list)
            callBack(filteredList)
            val l = searchLatestId(filteredList)
            if(l != null){
                latestId = l
            }
        }

    }
    override fun getOldItems(callBack:(List<E>)->Unit){
        if( oldestId == null ){
            init(null, callBack)
            return
        }

        if(requestOldestFlag == oldestId){
            Log.d("", "重複を防いだ")
            callBack(emptyList())
            return
        }else{
            requestOldestFlag = oldestId
        }
        GlobalScope.launch{
            val list = mRepository.getItemsUseUntilId(oldestId!!)
            if( list == null ){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@launch
            }
            val filteredList = mFilter.filter(list)
            callBack(filteredList)

            val o = searchOldestId(filteredList)
            if(o != null){
                oldestId = o
                requestOldestFlag = null
            }
        }
    }
    override fun init(initItem: E?, callBack:(List<E>)->Unit){
        /*GlobalScope.launch{
            val list = mRepository.getItems()
            if( list == null ){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@launch
            }

            val filteredList = mFilter.filter(list)
            callBack(filteredList)

            val l = searchLatestId(filteredList)
            val o = searchOldestId(filteredList)
            if(l != null){
                latestId = l
            }
            if(o != null){
                oldestId = o
            }
        }*/
        GlobalScope.launch{
            val list: List<E>
            if( initItem == null ){
                val items = mRepository.getItems()
                if( items == null ){
                    errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                    return@launch
                }

                list = mFilter.filter(items)

            }else{
                val tmpList = arrayListOf(initItem)
                val items = mRepository.getItemsUseUntilId(initItem.id)
                if( items == null ){
                    errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                    return@launch
                }
                tmpList.addAll(mFilter.filter(items))
                list = tmpList
            }
            callBack(list)

            val l = searchLatestId(list)
            val o = searchOldestId(list)
            if(l != null){
                latestId = l
            }
            if(o != null){
                oldestId = o
            }

        }

    }

    private fun searchLatestId(list: List<ID>): String?{
        for(n in list){

            if(! n.isIgnore ){
                return n.id
            }
        }
        return null
    }

    private fun searchOldestId(list: List<ID>): String?{
        for(n in (list.size - 1).downTo(0)){
            val data = list[n]
            /*if(!noteViewData.isOriginReply){
                return noteViewData
            }*/
            if(!data.isIgnore){
                return data.id
            }
        }
        return null
    }

}