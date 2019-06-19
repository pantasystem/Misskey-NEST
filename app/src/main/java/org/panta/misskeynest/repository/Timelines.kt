package org.panta.misskeynest.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.RequestTimelineProperty
import java.net.URL

class GlobalTimeline(private val domain: String, private val authKey: String) : AbsTimeline(URL("$domain/api/notes/global-timeline")){

    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("GlobalTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = authKey
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate


        return jacksonObjectMapper().writeValueAsString(rtp)
    }
}

class LocalTimeline(domain: String, private val authKey: String) : AbsTimeline(URL("$domain/api/notes/local-timeline")){
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

class HomeTimeline(domain: String, private val authKey: String) : AbsTimeline(URL("$domain/api/notes/timeline")) {

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

class SearchRepository(private val connectionProperty: ConnectionProperty, private val  keyword: String) : AbsTimeline(URL("${connectionProperty.domain}/api/notes/search")){
    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        val property = RequestTimelineProperty()
        property.i = connectionProperty.i
        property.limit = 20
        property.sinceId = sinceId
        property.apply {
            this.untilId = untilId
            this.sinceDate = sinceDate
            this.untilDate = untilDate
            this.query = keyword
        }
        return jacksonObjectMapper().writeValueAsString(property)
    }
}