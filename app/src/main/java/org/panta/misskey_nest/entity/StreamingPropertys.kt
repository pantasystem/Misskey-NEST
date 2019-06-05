package org.panta.misskey_nest.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

data class StreamingProperty(
    val type: String,
    val body: BodyProperty
)


@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true) data class BodyProperty(
    val id: String,
    val type: String? = null,
    val body: InsideBodyProperty? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)  data class InsideBodyProperty(
    val reaction: String? = null,
    val userId: String? = null,
    val choice: Int? = null,
    val deletedAt: String? = null
)