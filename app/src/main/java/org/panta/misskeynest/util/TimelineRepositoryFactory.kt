package org.panta.misskeynest.util

import org.panta.misskeynest.constant.TimelineTypeEnum
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.repository.remote.*

class TimelineRepositoryFactory(private val connectionInfo: ConnectionProperty){
    fun create(timelineType: TimelineTypeEnum): IItemRepository<Note>?{
        return when (timelineType) {
            TimelineTypeEnum.GLOBAL -> GlobalTimeline(connectionInfo)
            TimelineTypeEnum.HOME -> HomeTimeline(connectionInfo)
            TimelineTypeEnum.SOCIAL -> SocialTimeline(connectionInfo)
            TimelineTypeEnum.LOCAL -> LocalTimeline(connectionInfo)
            else -> throw IllegalArgumentException("global, home. social, localしか許可されていません。")
        }
    }

    fun create(user: User, isMediaOnly: Boolean, isPin: Boolean): IItemRepository<Note>{
        return if(isPin){
            UserPinNotes(connectionInfo, user)
        }else{
            UserTimeline(connectionInfo, user.id, isMediaOnly)
        }
    }

    fun create(keyWord: String, isMediaOnly: Boolean): IItemRepository<Note>{
        return SearchRepository(connectionInfo, keyWord)
    }
}