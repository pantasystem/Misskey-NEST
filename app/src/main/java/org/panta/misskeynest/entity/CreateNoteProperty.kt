package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL) data class CreateNoteProperty(
    @JsonProperty("i") val i: String?,
    @JsonProperty("visibility") val visibility: String = "public",
    @JsonProperty("visibleUserIds") val visibleUserIds: List<String>? = null,
    @JsonProperty("text") val text: String? = null,
    @JsonProperty("cw") val cw: String? = null,
    @JsonProperty("viaMobile") val viaMobile: Boolean? = null,
    @JsonProperty("localOnly") val localOnly: Boolean? = null,
    @JsonProperty("noExtractMentions") val noExtractMentions: Boolean? = null,
    @JsonProperty("noExtractEmojis") val noExtractEmojis: Boolean? = null,
    @JsonProperty("geo") val geo: String? = null,
    @JsonProperty("fileIds") val fileIds: List<String>? = null,
    @JsonProperty("replyId") val replyId: String? = null,
    @JsonProperty("renoteId") val renoteId: String? = null,
    @JsonProperty("poll") val poll: String? = null
): Serializable{
    class Builder(val i: String): Serializable{
        var visibility: String = "public"
        var visibleUserIds: List<String>? = null
        var text: String? = null
        var cw: String? = null
        var viaMobile: Boolean? = null
        var localOnly: Boolean? = null
        var noExtractMentions: Boolean? = null
        var noExtractEmojis: Boolean? = null
        var geo: String? = null
        var fileIds: List<String>? = null
        var replyId: String? = null
        var renoteId: String? = null
        var poll: String? = null

        fun create():CreateNoteProperty{
            return CreateNoteProperty(i = i,
                visibility = visibility,
                visibleUserIds = visibleUserIds,
                text = text,
                cw = cw,
                viaMobile = viaMobile,
                localOnly = localOnly,
                noExtractMentions = noExtractMentions,
                noExtractEmojis = noExtractEmojis,
                geo = geo,
                fileIds = fileIds,
                replyId = replyId,
                renoteId = renoteId,
                poll = poll
                )
        }
    }
}