package org.panta.misskeynest.interfaces

import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.FollowProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.view_data.FollowViewData

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