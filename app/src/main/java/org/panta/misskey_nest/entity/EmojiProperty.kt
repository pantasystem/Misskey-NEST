package org.panta.misskey_nest.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown=true) data class EmojiProperty(
    val id: String,
    val updatedAt: String,
    val name: String,
    val host: String?,
    val url: String?,
    val uri: String?,
    val type: String,
    val aliases: List<String>

)