package org.panta.misskeynest.repository.remote

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.RequestTimelineProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.IItemRepository
import java.net.URL

class GlobalTimeline(private val connectionProperty: ConnectionProperty)
    : AbsTimeline(URL("${connectionProperty.domain}/api/notes/global-timeline")){

    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("GlobalTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = connectionProperty.i
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate


        return jacksonObjectMapper().writeValueAsString(rtp)
    }
}

class LocalTimeline(private val connectionProperty: ConnectionProperty)
    : AbsTimeline(URL("${connectionProperty.domain}/api/notes/local-timeline")){
    private val mapper = jacksonObjectMapper()
    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("HomeTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = connectionProperty.i
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate


        return mapper.writeValueAsString(rtp)
    }
}

class HomeTimeline(private val connectionProperty: ConnectionProperty)
    : AbsTimeline(URL("${connectionProperty.domain}/api/notes/timeline")) {

    private val mapper = jacksonObjectMapper()

    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("HomeTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = connectionProperty.i
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate


        return mapper.writeValueAsString(rtp)
    }
}

class SocialTimeline(private val connectionProperty: ConnectionProperty)
    : AbsTimeline(URL("${connectionProperty.domain}/api/notes/hybrid-timeline")){

    private val mapper = jacksonObjectMapper()

    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("HomeTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = connectionProperty.i
        rtp.limit = 20
        rtp.sinceId = sinceId
        rtp.untilId = untilId
        rtp.sinceDate = sinceDate
        rtp.untilDate = untilDate


        return mapper.writeValueAsString(rtp)
    }
}

class UserTimeline(private val connectionProperty: ConnectionProperty , private val userId: String, private val isMediaOnly: Boolean? = null)
    : AbsTimeline(URL("${connectionProperty.domain}/api/users/notes")){


    override fun createRequestTimelineJson(
        sinceId: String?,
        untilId: String?,
        sinceDate: Long?,
        untilDate: Long?
    ): String {
        Log.d("UserTimeline", "値は sinceId:$sinceId, untilId:$untilId, sinceDate:$sinceDate ,untilDate:$untilDate, ")
        val rtp = RequestTimelineProperty()
        rtp.i = connectionProperty.i
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

class UserPinNotes(private val connectionProperty: ConnectionProperty ,private val user: User):
    IItemRepository<Note> {
    override fun getItems(): List<Note>? {
        //return user.pinnedNotes
        return user.pinnedNotes ?: try{
            val map = mapOf("i" to connectionProperty.i, "userId" to user.id)
            val json = jacksonObjectMapper().writeValueAsString(map)
            val response = OkHttpConnection().postString(URL("${connectionProperty.domain}/api/users/show"), json)?: return null
            val user: User = jacksonObjectMapper().readValue(response)
            return user.pinnedNotes
        }catch(e: Exception){
            Log.d("UserPinNotes" ,"error", e)
            null
        }
    }

    override fun getItemsUseSinceId(sinceId: String): List<Note>? {
        return null
    }

    override fun getItemsUseUntilId(untilId: String): List<Note>? {
        return null
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