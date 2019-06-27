package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

data class StreamingProperty<E>(
    val type: String,
    val body: BodyProperty<E>
)


@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true) data class BodyProperty<E>(
    val id: String,
    val channel: String? = null,
    val type: String? = null,
    val body: E? = null,
    val params: Map<String, Any?>? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)  data class NoteUpdatedProperty(
    val reaction: String? = null,
    val userId: String? = null,
    val choice: Int? = null,
    val deletedAt: String? = null
)