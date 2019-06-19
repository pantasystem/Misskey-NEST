package org.panta.misskeynest.util

import org.panta.misskeynest.constant.TimelineTypeEnum
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.repository.GlobalTimeline
import org.panta.misskeynest.repository.HomeTimeline
import org.panta.misskeynest.repository.LocalTimeline
import org.panta.misskeynest.repository.SocialTimeline

class PopularTimelineRepositoryFactory(private val connectionInfo: ConnectionProperty){
    fun create(mTimelineType: TimelineTypeEnum): IItemRepository<Note>?{
        return when (mTimelineType) {
            TimelineTypeEnum.GLOBAL -> GlobalTimeline(domain = connectionInfo.domain , authKey = connectionInfo.i)
            TimelineTypeEnum.HOME -> HomeTimeline(domain = connectionInfo.domain  , authKey = connectionInfo.i)
            TimelineTypeEnum.SOCIAL -> SocialTimeline(domain = connectionInfo.domain  , authKey = connectionInfo.i)
            TimelineTypeEnum.LOCAL -> LocalTimeline(domain = connectionInfo.domain, authKey = connectionInfo.i)
            else -> null
        }
    }
}