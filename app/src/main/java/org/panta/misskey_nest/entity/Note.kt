package org.panta.misskey_nest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown=true) data class Note(
    @JsonProperty("id") val id: String,
    @JsonProperty("createdAt") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") val createdAt: Date,
    @JsonProperty("text") val text: String?,
    @JsonProperty("cw") val cw: String?,
    @JsonProperty("userId") val userId: String?,

    @JsonProperty("replyId") val replyId: String?,
    @JsonProperty("renoteId") val reNoteId: String?,
    @JsonProperty("viaMobile") val viaMobile: Boolean?,
    @JsonProperty("visibility") val visibility: String?,
    @JsonProperty("visibilityUserIds") val visibilityUserIds: List<String?>?,
    @JsonProperty("url") val url: String?,
    @JsonProperty("renoteCount") val reNoteCount: Int,
    @JsonProperty("reactions") val reactionCounts: Map<String, Int>?,
    @JsonProperty("emojis") val emojis: List<EmojiProperty?>?,
    @JsonProperty("repliesCount") val replyCount: Int,
    @JsonProperty("user") val user: User?,
    @JsonProperty("files") val files: List<FileProperty?>?,
    //@JsonProperty("fileIds") val mediaIds: List<String?>?,    //v10, v11の互換性が取れない
    @JsonProperty("renote") val renote: Note?,
    @JsonProperty("reply") val reply: Note?,
    @JsonProperty("myReaction") val myReaction: String?
): Serializable