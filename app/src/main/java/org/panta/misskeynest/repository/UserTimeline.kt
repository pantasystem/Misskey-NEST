package org.panta.misskeynest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.panta.misskeynest.entity.RequestTimelineProperty
import java.lang.StringBuilder
import java.net.URL

class UserTimeline(domain: String, private val userId: String, private val isMediaOnly: Boolean? = null) : AbsTimeline(URL("$domain/api/users/notes")){


    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("UserTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate
        rtp.userId = userId
        rtp.withFiles = isMediaOnly

        return jacksonObjectMapper().writeValueAsString(rtp)

        //json.
    }
}