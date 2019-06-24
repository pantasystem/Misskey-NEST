package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.panta.misskeynest.constant.REMOTE_DATE_FORMAT
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown=true)  data class FileProperty(
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("createdAt") @JsonFormat(pattern = REMOTE_DATE_FORMAT) val createdAt: Date?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("type") val type: String? = null,
    @JsonProperty("md5") val md5: String? = null,
    @JsonProperty("size") val size: Int? = null,
    @JsonProperty("userId") val userId: String? = null,
    @JsonProperty("folderId") val folderId: String? = null,
    @JsonProperty("comment") val comment: String? = null,
    @JsonProperty("properties") val properties: Property? = null,
    @JsonProperty("isSensitive") val isSensitive: Boolean? = null,
    @JsonProperty("url") val url: String? = null,
    @JsonProperty("webpublicUrl") val webPublicUrl: String? = null,
    @JsonProperty("thumbnalUrl") val thumbnalUrl: String? = null,
    @JsonProperty("attachedNoteIds") val attachedNoteIds: List<String?>? = null
):Serializable