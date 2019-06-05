package org.panta.misskey_nest.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL) data class RequestNotificationProperty(
    @JsonProperty("i") val i: String,
    @JsonProperty("limit") val limit: Int? = null,
    @JsonProperty("sinceId") val sinceId: String? = null,
    @JsonProperty("untilId") val untilId: String? = null,
    @JsonProperty("following") val isFollowing: Boolean? = null,
    @JsonProperty("markAsRead") val isMarkAsRead: Boolean? = null,
    @JsonProperty("includeTypes") val includeTypes: List<String?>? = null,
    @JsonProperty("excludeTypes") val excludeTypes: List<String?>? = null
)