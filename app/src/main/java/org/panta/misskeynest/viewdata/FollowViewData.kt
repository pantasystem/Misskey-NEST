package org.panta.misskeynest.viewdata

import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.ID

data class FollowViewData(override val id: String, override val isIgnore: Boolean, val following: User?,val follower: User?):ID