package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.panta.misskeynest.constant.REMOTE_DATE_FORMAT
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown=true) data class EmojiProperty(
    val id: String?,
    @JsonFormat(pattern = REMOTE_DATE_FORMAT) val updatedAt: String?,
    val name: String,
    val host: String?,
    val url: String?,
    val uri: String?,
    val type: String?
    //val aliases: List<String>

): Serializable