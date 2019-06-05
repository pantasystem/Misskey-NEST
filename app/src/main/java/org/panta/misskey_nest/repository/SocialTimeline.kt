package org.panta.misskey_nest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.panta.misskey_nest.entity.RequestTimelineProperty
import java.net.URL

class SocialTimeline(domain: String, private val authKey: String) : AbsTimeline(URL("$domain/api/notes/hybrid-timeline")){

    private val mapper = jacksonObjectMapper()

    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("HomeTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = authKey
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate


        return mapper.writeValueAsString(rtp)
    }
}