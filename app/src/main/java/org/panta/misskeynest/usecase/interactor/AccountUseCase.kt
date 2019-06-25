package org.panta.misskeynest.usecase.interactor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.repository.IAccountRepository
import org.panta.misskeynest.usecase.IAccountUseCase

class AccountUseCase(private val mAccountRepository: IAccountRepository, private val errorHandler: ErrorCallBackListener?) : IAccountUseCase{
    override fun getMyInfo(callBack: (User?) -> Unit) {
        GlobalScope.launch {
            try{
                val user = mAccountRepository.getMyInfo()
                callBack(user)
            }catch(e: Exception){
                callBack(null)
                errorHandler?.callBack(e)
            }
        }
    }
}