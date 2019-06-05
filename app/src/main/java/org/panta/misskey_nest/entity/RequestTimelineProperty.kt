package org.panta.misskey_nest.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable



@JsonInclude(JsonInclude.Include.NON_NULL) data class RequestTimelineProperty(
    @JsonProperty("i") var i: String? = null,
    @JsonProperty("userId") var userId: String? = null,
    @JsonProperty("withFiles") var withFiles: Boolean? = null,
    @JsonProperty("fileType") var fileType: String? = null,
    @JsonProperty("excludeNsfw") var excludeNsfw: Boolean? = null,
    @JsonProperty("limit") var limit: Int? = 10,
    @JsonProperty("sinceId") var sinceId: String? = null,
    @JsonProperty("untilId") var untilId: String? = null,
    @JsonProperty("sinceDate") var sinceDate: Long? = null,
    @JsonProperty("untilDate") var untilDate: Long? = null
): Serializable