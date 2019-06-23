package org.panta.misskeynest.listener

import android.content.Context
import org.panta.misskeynest.UserActivity
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.IUserClickListener

class UserClickListener(private val context: Context) : IUserClickListener{
    override fun onClickedUser(user: User) {
        context.startActivity(UserActivity.getIntent(context, user))
    }
}