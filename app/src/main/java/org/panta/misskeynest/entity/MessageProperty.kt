package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) data class MessageProperty(
    @JsonProperty("id") val id: String,
    @JsonProperty("createdAt") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") val createdAt: Date,
    @JsonProperty("text") val text: String?,
    @JsonProperty("userId") val userId: String?,
    @JsonProperty("user") val user: User?,
    @JsonProperty("recipientId") val recipientId: String?,
    @JsonProperty("groupId") val groupId: String?,
    @JsonProperty("fileId") val fileId: String?,
    @JsonProperty("file") val file: FileProperty?,
    @JsonProperty("isRead") val isRead: Boolean?
)