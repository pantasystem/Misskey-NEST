package org.panta.misskeynest.interactor

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.repository.INoteRepository
import org.panta.misskeynest.usecase.INoteUseCase
import org.panta.misskeynest.viewdata.NoteViewData

class NoteUseCase(private val mNoteRepository: INoteRepository, private val errorHandler: ErrorCallBackListener?) : INoteUseCase{

    private val viewDataFormat = NoteFormatUseCase()

    override fun getNote(noteId: String, callBack: (NoteViewData?) -> Unit){
        GlobalScope.launch{
            try{
                val note = mNoteRepository.getNote(noteId)
                if( note == null ){
                    callBack(null)
                    return@launch
                }
                callBack(viewDataFormat.createViewData(note))
            }catch(e: Exception){
                errorHandler?.callBack(e)
                callBack(null)
            }
        }
    }

    override fun remove(note: Note, callBack: (Boolean) -> Unit) {
        GlobalScope.launch{
            try{
                callBack(mNoteRepository.remove(note))
            }catch( e: Exception ){
                errorHandler?.callBack(e)
                callBack(false)
            }
        }
    }

    override fun send(property: CreateNoteProperty, callBack: (Boolean) -> Unit) {
        GlobalScope.launch{
            try{
                callBack(mNoteRepository.send(property))
            }catch( e: Exception ){
                errorHandler?.callBack(e)
            }
        }
    }

    override fun getNoteDetail(currentNote: Note, callBack: (List<NoteViewData>) -> Unit) {
        GlobalScope.launch{

            try{
                val notesList = ArrayList<NoteViewData>()
                //ReplyToを展開する
                //ReplyToのIDをもとにConversationから取得する

                val replyTo = currentNote.reply
                if( replyTo != null ){


                    val conversation = mNoteRepository.getConversation(replyTo.id)
                    if( conversation != null ) {
                        for (i in ( conversation.size - 1 ) downTo 0) {
                            val vd = viewDataFormat.createViewData(conversation[i])
                            if (vd != null) notesList.add(vd)
                        }
                    }

                    val replyToViewData = viewDataFormat.createViewData(replyTo)

                    if(replyToViewData != null) notesList.add(replyToViewData)


                }
                val currentNoteViewData = viewDataFormat.createViewData(currentNote)!!
                notesList.add(currentNoteViewData)


                val childNote = mNoteRepository.getChild(currentNote.id)

                if( childNote != null ){
                    for( n in childNote ){
                        val vd = viewDataFormat.createViewData(n)
                        if( vd != null ) notesList.add(vd)
                    }
                }

                callBack(notesList)
            }catch(e: Exception){
                Log.d("GetNoteDetail", "error", e)
            }


        }
    }
}