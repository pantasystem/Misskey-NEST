package org.panta.misskey_nest.interfaces

import android.os.Handler
import org.panta.misskey_nest.view_data.NoteViewData

interface IOperationAdapter<E> {
    fun addAllFirst(list: List<E>)

    fun addAllLast(list: List<E>)

    fun getItem(index: Int): E?
    fun getItem(item: E): E?
    fun updateItem(item: E)

    fun removeItem(item: E)
}