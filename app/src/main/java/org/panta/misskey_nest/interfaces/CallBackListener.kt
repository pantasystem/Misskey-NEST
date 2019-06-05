package org.panta.misskey_nest.interfaces

interface CallBackListener<E> {
    fun callBack(e: E)
}