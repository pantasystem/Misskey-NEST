package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true) data class ClientSetting(
    @JsonProperty("home") val home: List<Home?>? = null,
    @JsonProperty("mobileHome") val mobileHome: List<Home?>? = null,
    @JsonProperty("dark") val isDark: Boolean,
    @JsonProperty("circleIcons") val isCircleIcons: Boolean,
    @JsonProperty("showMyRenotes") val isShowMyReNotes: Boolean,
    @JsonProperty("showReplyTarget") val isShowReplyTarget: Boolean

)

@JsonIgnoreProperties(ignoreUnknown = true) data class Home(
    @JsonProperty("name") val name: String? = null,
    @JsonProperty("id") val id: String? = null,
    @JsonProperty("place") val place: String? = null,
    @JsonProperty("data") val data: Data? = null
)

@JsonIgnoreProperties(ignoreUnknown = true) data class Data(
    @JsonProperty("compact") val isCompact: Boolean? = null,
    @JsonProperty("design") val design: Int? = null,
    @JsonProperty("url") val url: String? = null
)