package org.panta.misskeynest.usecase

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.ID
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.interfaces.IPaging

class PagingController<E: ID>(private val mRepository: IItemRepository<E>, private val errorCallBackListener: ErrorCallBackListener): IPaging<E>{
    private var latestId: String? = null
    private var oldestId: String? = null

    private var requestOldestFlag: String? = null

    override fun getNewItems(callBack:(List<E>)->Unit){
        if(latestId == null){
            init(callBack)
            return
        }
        GlobalScope.launch{
            val list = mRepository.getItemsUseSinceId(latestId!!)
            if(list == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@launch
            }
            callBack(list)
            val l = searchLatestId(list)
            if(l != null){
                latestId = l
            }
        }

    }
    override fun getOldItems(callBack:(List<E>)->Unit){
        if( oldestId == null ){
            init(callBack)
            return
        }

        if(requestOldestFlag == oldestId){
            Log.d("PagingController", "重複を防いだ")
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
            callBack(list)

            val o = searchOldestId(list)
            if(o != null){
                oldestId = o
                requestOldestFlag = null
            }
        }
    }
    override fun init(callBack:(List<E>)->Unit){
        GlobalScope.launch{
            val list = mRepository.getItems()
            if( list == null ){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@launch
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