package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.panta.misskeynest.constant.REMOTE_DATE_FORMAT
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupProperty (
    val id: String,
    @JsonFormat(pattern = REMOTE_DATE_FORMAT) val createdAt: Date?,
    val name: String,
    val ownerId: String?,
    val userIds: List<String>?

): Serializable