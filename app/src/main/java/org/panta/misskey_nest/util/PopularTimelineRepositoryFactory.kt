package org.panta.misskey_nest.util

import com.google.android.gms.nearby.connection.ConnectionInfo
import org.panta.misskey_nest.constant.TimelineTypeEnum
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.interfaces.IItemRepository
import org.panta.misskey_nest.repository.*
import org.panta.misskey_nest.view_data.NoteViewData

class PopularTimelineRepositoryFactory(private val connectionInfo: ConnectionProperty){
    fun create(mTimelineType: TimelineTypeEnum): IItemRepository<NoteViewData>?{
        return when (mTimelineType) {
            TimelineTypeEnum.GLOBAL -> GlobalTimeline(domain = connectionInfo.domain , authKey = connectionInfo.i)
            TimelineTypeEnum.HOME -> HomeTimeline(domain = connectionInfo.domain  , authKey = connectionInfo.i)
            TimelineTypeEnum.SOCIAL -> SocialTimeline(domain = connectionInfo.domain  , authKey = connectionInfo.i)
            TimelineTypeEnum.LOCAL -> LocalTimeline(domain = connectionInfo.domain, authKey = connectionInfo.i)
            else -> null
        }
    }
}