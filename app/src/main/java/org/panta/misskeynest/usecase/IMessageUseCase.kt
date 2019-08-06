package org.panta.misskeynest.usecase

interface IMessageUseCase {
    val isGroup: Boolean
    //val roomId: String
    fun sendMessage(text: String, callBack: (Boolean) -> Unit)
    fun sendFile(fileId: String, callBack: (Boolean) -> Unit)
}