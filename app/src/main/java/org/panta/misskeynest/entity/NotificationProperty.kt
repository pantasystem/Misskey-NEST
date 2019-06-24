package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.panta.misskeynest.constant.REMOTE_DATE_FORMAT
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) data class NotificationProperty(
    @JsonProperty("createdAt") @JsonFormat(pattern = REMOTE_DATE_FORMAT) val createdAt: Date,
    @JsonProperty("type") val type: String,
    @JsonProperty("isRead") val isRead: Boolean,
    @JsonProperty("noteId") val noteId: String? = null,
    @JsonProperty("id") val id: String,
    @JsonProperty("userId") val userId: String,
    @JsonProperty("user") val user: User,
    @JsonProperty("note") val note: Note?,
    @JsonProperty("reaction") val reaction: String?
)