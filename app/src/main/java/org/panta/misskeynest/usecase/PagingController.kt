package org.panta.misskeynest.usecase

import android.util.Log
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
        mRepository.getItemsUseSinceId(latestId!!){
            if(it == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@getItemsUseSinceId
            }
            callBack(it)
            val l = searchLatestId(it)
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
        mRepository.getItemsUseUntilId(oldestId!!){
            if(it == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@getItemsUseUntilId
            }
            callBack(it)

            val o = searchOldestId(it)
            if(o != null){
                oldestId = o
                requestOldestFlag = null
            }
        }
    }
    override fun init(callBack:(List<E>)->Unit){
        mRepository.getItems {
            if(it == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@getItems
            }
            callBack(it)

            val l = searchLatestId(it)
            val o = searchOldestId(it)
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