package org.panta.misskeynest.interactor

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.repository.remote.NoteDetail
import org.panta.misskeynest.viewdata.NoteViewData

class GetNoteDetail(conProperty: ConnectionProperty) {

    private val noteJus = NoteFormatUseCase()

    private val mNoteDetail = NoteDetail(conProperty)

    fun get(currentNote: Note ,callBack:(List<NoteViewData>)-> Unit) = GlobalScope.launch{

        try{
            val notesList = ArrayList<NoteViewData>()
            //ReplyToを展開する
            //ReplyToのIDをもとにConversationから取得する

            val replyTo = currentNote.reply
            if( replyTo != null ){


                val conversation = mNoteDetail.getConversation(replyTo.id)
                if( conversation != null ) {
                    for (i in ( conversation.size - 1 ) downTo 0) {
                        val vd = noteJus.createViewData(conversation[i])
                        if (vd != null) notesList.add(vd)
                    }
                }

                val replyToViewData = noteJus.createViewData(replyTo)

                if(replyToViewData != null) notesList.add(replyToViewData)


            }
            val currentNoteViewData = noteJus.createViewData(currentNote)!!
            notesList.add(currentNoteViewData)


            val childNote = mNoteDetail.getChild(currentNote.id)

            if( childNote != null ){
                for( n in childNote ){
                    val vd = noteJus.createViewData(n)
                    if( vd != null ) notesList.add(vd)
                }
            }



            callBack(notesList)
        }catch(e: Exception){
            Log.d("GetNoteDetail", "error", e)
        }


    }


}