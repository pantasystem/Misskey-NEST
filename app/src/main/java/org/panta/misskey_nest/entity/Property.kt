package org.panta.misskey_nest.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown=true) data class Property(
    @JsonProperty("width") val width: Int? =null,
    @JsonProperty("height") val height: Int? = null
    //@JsonProperty("avgColor") val avgColor: String? = null    //FIXME v11, v10の互換性が取れない
):Serializable