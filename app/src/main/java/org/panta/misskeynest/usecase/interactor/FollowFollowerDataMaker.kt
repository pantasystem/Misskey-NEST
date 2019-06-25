package org.panta.misskeynest.usecase.interactor

import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.FollowProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.viewdata.FollowViewData

class FollowFollowerDataMaker : AbsFollowViewDataMaker(){
    override fun createList(baseList: List<FollowProperty>, addList: List<User?>, type: FollowFollowerType): List<FollowViewData> {
        return baseList.map{
            if(type == FollowFollowerType.FOLLOWER){
                val following = addList.firstOrNull{user ->
                    user != null && user.id == it.follower?.id
                }
                FollowViewData(it.id, false, follower = it.follower, following = following)
            }else{
                val follower = addList.firstOrNull { user ->
                    user != null && user.id == it.followee?.id
                }
                FollowViewData(it.id, false, following = it.followee, follower = follower)
            }
        }
    }
}