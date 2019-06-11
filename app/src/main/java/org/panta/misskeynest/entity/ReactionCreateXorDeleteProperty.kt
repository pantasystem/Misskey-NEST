package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL) data class ReactionCreateXorDeleteProperty(
    @JsonProperty("i") val i: String,
    @JsonProperty("noteId") val noteId: String,
    @JsonProperty("reaction") val reaction: String? = null
    ): Serializable