package org.panta.misskey_nest.interfaces

import org.panta.misskey_nest.entity.User

//別の独自のインターフェースと統合予定
interface UserClickListener {
    fun onClickedUser(user: User)
}