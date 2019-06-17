package org.panta.misskeynest.interfaces

interface IOperationAdapter<E> {
    fun addAllFirst(list: List<E>)

    fun addAllLast(list: List<E>)

    fun getItem(index: Int): E?
    fun getItem(item: E): E?
    fun getItem(id: String): E?

    fun updateItem(item: E)

    fun removeItem(item: E)
}