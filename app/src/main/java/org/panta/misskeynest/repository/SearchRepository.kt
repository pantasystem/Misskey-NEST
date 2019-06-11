package org.panta.misskeynest.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Job
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.RequestTimelineProperty
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.view_data.NoteViewData
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