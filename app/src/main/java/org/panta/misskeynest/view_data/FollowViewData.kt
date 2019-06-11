package org.panta.misskeynest.view_data

import org.panta.misskeynest.entity.FollowProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.ID

data class FollowViewData(override val id: String, override val isIgnore: Boolean, val following: User?,val follower: User?):ID