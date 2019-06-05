package org.panta.misskey_nest.view_data

import org.panta.misskey_nest.entity.FollowProperty
import org.panta.misskey_nest.entity.User
import org.panta.misskey_nest.interfaces.ID

data class FollowViewData(override val id: String, override val isIgnore: Boolean, val following: User?,val follower: User?):ID