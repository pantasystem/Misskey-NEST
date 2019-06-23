package org.panta.misskeynest.usecase

import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.viewdata.NoteViewData

interface INoteCaptureUseCase {
    val mAdapterOperator: IOperationAdapter<NoteViewData>

    fun start()

    fun add(viewData: NoteViewData)
    fun addAll(list: List<NoteViewData>)
    fun clear()
    fun remove(viewData: NoteViewData)




}