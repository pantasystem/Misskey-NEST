package org.panta.misskeynest.listener

import android.content.Context
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.IUserClickListener
import org.panta.misskeynest.UserActivity

class UserClickListener(private val context: Context) : IUserClickListener{
    override fun onClickedUser(user: User) {
        UserActivity.startActivity(context, user)
    }
}