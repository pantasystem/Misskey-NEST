package org.panta.misskeynest.usecase

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.view_data.NoteViewData

class GetNoteDetail(private var mCurrentNoteId: String) {



    fun getChilds(childNoteId: String, callBack: (NoteViewData)->Unit) = GlobalScope.launch {
        try{

        }catch(e: Exception){

        }
    }

    fun getParents(callBack:(NoteViewData)-> Unit) = GlobalScope.launch{
        try{

        }catch(e: Exception){

        }
    }
}