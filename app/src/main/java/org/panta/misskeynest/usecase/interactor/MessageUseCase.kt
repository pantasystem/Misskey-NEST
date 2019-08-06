package org.panta.misskeynest.usecase.interactor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.repository.remote.MessageRepository
import org.panta.misskeynest.usecase.IMessageUseCase

class MessageUseCase(private val connectionProperty: ConnectionProperty, private val groupId: String?, private val userId: String?) : IMessageUseCase{

    private val mRepository = MessageRepository(connectionProperty)
    override val isGroup: Boolean
        get() = groupId != null

    override fun sendFile(fileId: String, callBack: (Boolean) -> Unit) {
        GlobalScope.launch{
            val res = if(isGroup){
                mRepository.create(userId = null, groupId = groupId, fileId = fileId, text= null)
            }else{
                mRepository.create(userId = userId, groupId = null, fileId = fileId, text= null)
            }

            callBack(res)
        }
    }

    override fun sendMessage(text: String, callBack: (Boolean) -> Unit) {
        GlobalScope.launch{
            val res = if(isGroup){
                mRepository.create(userId = null, groupId = groupId, fileId = null, text= text)
            }else{
                mRepository.create(userId = userId, groupId = null, fileId = null, text= text)
            }
            callBack(res)
        }

    }

}