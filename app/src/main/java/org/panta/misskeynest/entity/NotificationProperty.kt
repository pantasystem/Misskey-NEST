package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true) data class NotificationProperty(
    @JsonProperty("createdAt") val createdAt: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("isRead") val isRead: Boolean,
    @JsonProperty("noteId") val noteId: String? = null,
    @JsonProperty("id") val id: String,
    @JsonProperty("userId") val userId: String,
    @JsonProperty("user") val user: User,
    @JsonProperty("note") val note: Note?,
    @JsonProperty("reaction") val reaction: String?
)