package org.panta.misskey_nest.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Job
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.entity.RequestTimelineProperty
import org.panta.misskey_nest.interfaces.IItemRepository
import org.panta.misskey_nest.view_data.NoteViewData
import java.net.URL

class SearchRepository(private val connectionProperty: ConnectionProperty,private val  keyword: String) : AbsTimeline(URL("${connectionProperty.domain}/api/notes/search")){
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