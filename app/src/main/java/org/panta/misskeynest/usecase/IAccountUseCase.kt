package org.panta.misskeynest.usecase

import org.panta.misskeynest.entity.User

interface IAccountUseCase {
    fun getMyInfo(callBack: (User?)->Unit)
}