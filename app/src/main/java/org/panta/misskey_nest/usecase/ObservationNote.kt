package org.panta.misskey_nest.usecase

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.interfaces.IBindScrollPosition
import org.panta.misskey_nest.interfaces.IBindStreamingAPI
import org.panta.misskey_nest.repository.NoteCapture
import org.panta.misskey_nest.repository.NoteRepository
import org.panta.misskey_nest.view_data.NoteViewData
import java.util.*
import kotlin.collections.HashMap

class ObservationNote(private val bindStreamingAPI: IBindStreamingAPI, private val bindScrollPosition: IBindScrollPosition, private val info: ConnectionProperty) {

    private val capture = NoteCapture(info,bindStreamingAPI, bindScrollPosition)

    var isObserve: Boolean = true
    //このスピードでノートのキャプチャを登録するかを判定する
    private var scrollSpeed: Double = 0.0
    init{
        GlobalScope.launch{
            var beforePosition = bindScrollPosition.bindFirstVisibleItemPosition()?:0
            while(isObserve){
                delay(500)
                val nowPosition = bindScrollPosition.bindFirstVisibleItemPosition()?:0
                scrollSpeed = Math.abs(nowPosition - beforePosition) / 0.5
                beforePosition = nowPosition

                if(scrollSpeed < 3.0){
                    try{
                        captureNote()
                    }catch(e: Exception){
                        Log.w("Observation", "error", e)
                    }
                }

            }

        }
    }

    private var beforeFirst = 0
    private var beforeEnd = 0
    @SuppressLint("UseSparseArrays")
    private val captureNoteMap = HashMap<Int, NoteViewData>()

    fun onRefresh(){
        beforeFirst = 0
        beforeEnd = 0
        synchronized(captureNoteMap){
            capture.clearCapture()
            captureNoteMap.clear()
        }
    }

    private fun captureNote(){
        val firstVisiblePosition = bindScrollPosition.bindFirstVisibleItemPosition()?: 0
        //val visibleTotal = bindScrollPosition.bindFindItemCount()?: 0

        val end = getEnd()

        if(beforeFirst == firstVisiblePosition && beforeEnd == end){
            return
        }

        //登録
        for(n in firstVisiblePosition.until(end)){
            val note = bindScrollPosition.pickViewData(n)

            if(note != null){
                registerNote(n,note)
            }
        }

        //val isUp = beforeFirst - firstVisiblePosition > 0
        if(isUp()){
            //endを消す
            for(n in (end + 1)..beforeEnd){
                cancelCapture(n)
            }
        }else{
            //firstを消す
            for(n in beforeFirst.until(firstVisiblePosition)){
                cancelCapture(n)
            }
        }


        beforeFirst = firstVisiblePosition
        beforeEnd = end


    }

    private fun registerNote(index: Int,viewData: NoteViewData){
        synchronized(captureNoteMap){

            val hasNote = captureNoteMap.containsValue(viewData)
            if(hasNote){
                return
            }else{
                val count = captureNoteMap.count{
                    it.value.toShowNote.id == viewData.toShowNote.id
                }
                if(count > 0 ){
                    Log.d("Observation", "登録回避")

                }else{
                    Log.d("Observation", "登録した")
                    captureNoteMap.put(index, viewData)
                    capture.captureNote(viewData)
                }
            }
        }
    }

    private fun cancelCapture(index: Int){
        synchronized(captureNoteMap){
            Log.d("Observation", "解除した")
            val removeItem = captureNoteMap[index]
            captureNoteMap.remove(index)
            if(removeItem != null){
                capture.unCaptureNote(removeItem)
            }
        }
    }

    private fun isUp(): Boolean{
        val firstVisiblePosition = bindScrollPosition.bindFirstVisibleItemPosition()?: 0
        return beforeFirst - firstVisiblePosition > 0
    }

    private fun getEnd(): Int{
        val firstVisiblePosition = bindScrollPosition.bindFirstVisibleItemPosition()?: 0
        val visibleTotal = bindScrollPosition.bindFindItemCount()?: 0

        return firstVisiblePosition + visibleTotal
    }

    /*private fun reacquireNote(){
        if(beforeFirst == bindScrollPosition.bindFirstVisibleItemPosition() && beforeEnd == getEnd()){
            return
        }
        val position = (if(isUp()){
            bindScrollPosition.bindFirstVisibleItemPosition()
        }else{
            getEnd() - 1
        }) ?: return

        val data = bindScrollPosition.pickViewData(position) ?: return
        val time = Date().time - data.updatedAt.time
        val isShouldUpdate = time > (1000 * 60 * 60)

        if(isShouldUpdate){
            Log.d("Observation", "更新を開始")
            NoteRepository(info).getNote(data.id){
                if(it != null){
                    bindStreamingAPI.onUpdateNote(it)
                }
            }
        }



    }*/

}