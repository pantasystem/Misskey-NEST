package org.panta.misskey_nest.usecase

import org.panta.misskey_nest.constant.FollowFollowerType
import org.panta.misskey_nest.entity.FollowProperty
import org.panta.misskey_nest.entity.User
import org.panta.misskey_nest.interfaces.AbsFollowViewDataMaker
import org.panta.misskey_nest.view_data.FollowViewData

class FollowViewDataMaker : AbsFollowViewDataMaker(){
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