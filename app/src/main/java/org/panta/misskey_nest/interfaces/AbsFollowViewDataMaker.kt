package org.panta.misskey_nest.interfaces

import org.panta.misskey_nest.constant.FollowFollowerType
import org.panta.misskey_nest.entity.FollowProperty
import org.panta.misskey_nest.entity.User
import org.panta.misskey_nest.view_data.FollowViewData

abstract class AbsFollowViewDataMaker{

    fun createFollowingViewDataList(followingList: List<FollowProperty>, followerList: List<FollowProperty>): List<FollowViewData>{
        val followerUsers = followerList.map{
            it.follower
        }
        return createList(followingList, followerUsers, FollowFollowerType.FOLLOWING)
    }

    fun createFollowerViewDataList(followingList: List<FollowProperty>, followerList: List<FollowProperty>): List<FollowViewData>{
        val followingUsers = followingList.map{
            it.followee
        }
        return createList(followerList, followingUsers, FollowFollowerType.FOLLOWER)
    }

    abstract fun createList(baseList: List<FollowProperty>, addList: List<User?>, type: FollowFollowerType): List<FollowViewData>
}