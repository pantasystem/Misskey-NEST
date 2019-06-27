package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.panta.misskeynest.constant.REMOTE_DATE_FORMAT
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) data class MessageProperty(
    @JsonProperty("id") val id: String,
    @JsonProperty("createdAt") @JsonFormat(pattern = REMOTE_DATE_FORMAT) val createdAt: Date,
    @JsonProperty("text") val text: String?,
    @JsonProperty("userId") val userId: String?,
    @JsonProperty("user") val user: User?,
    @JsonProperty("recipientId") val recipientId: String?,
    @JsonProperty("recipient") val recipient: User?,
    @JsonProperty("groupId") val groupId: String?,
    @JsonProperty("group") val group: GroupProperty?,
    @JsonProperty("fileId") val fileId: String?,
    @JsonProperty("file") val file: FileProperty?,
    @JsonProperty("isRead") val isRead: Boolean?
): Serializable