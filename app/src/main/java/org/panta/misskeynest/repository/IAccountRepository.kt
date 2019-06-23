package org.panta.misskeynest.repository

import org.panta.misskeynest.entity.User

interface IAccountRepository {
    fun getMyInfo():User?
}