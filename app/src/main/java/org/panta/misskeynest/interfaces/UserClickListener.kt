package org.panta.misskeynest.interfaces

import org.panta.misskeynest.entity.User

//別の独自のインターフェースと統合予定
interface IUserClickListener {
    fun onClickedUser(user: User)
}