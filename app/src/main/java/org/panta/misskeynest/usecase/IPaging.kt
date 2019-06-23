package org.panta.misskeynest.usecase

interface IPaging<E>{
    fun init(callBack: (List<E>)->Unit)
    fun getNewItems(callBack: (List<E>)->Unit)
    fun getOldItems(callBack: (List<E>)->Unit)

}