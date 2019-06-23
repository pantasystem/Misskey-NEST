package org.panta.misskeynest.interactor

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
}