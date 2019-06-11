package org.panta.misskeynest.interfaces

import java.lang.Exception

//Adapter的な役割を持ち外部ライブラリの変更による影響を受けにくくする
interface WSListener {
    fun onOpen()
    fun onMessage(text: String)
    fun onClose()
    fun onError(e: Exception)

}