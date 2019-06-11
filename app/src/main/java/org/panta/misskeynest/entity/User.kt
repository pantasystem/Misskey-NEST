package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown=true) data class User(
    @JsonProperty("id") val id:String,
    @JsonProperty("username") val userName: String,
    @JsonProperty("name") val name: String?,
    @JsonProperty("host") val host: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("createdAt") val createdAt: String?,
    @JsonProperty("followersCount") val followersCount: Int,
    @JsonProperty("followingCount") val followingCount: Int,
    @JsonProperty("hostLower") val hostLower: String?,
    @JsonProperty("notesCount") val notesCount: Int,
    @JsonProperty("clientSettings") val clientSettings: ClientSetting?,
    @JsonProperty("email") val email: String?,
    @JsonProperty("isBot") val isBot: Boolean,
    @JsonProperty("isCat") val isCat: Boolean,
    @JsonProperty("lastUsedAt") val lastUsedAt: String?,
    @JsonProperty("line") val line: String?,
    @JsonProperty("links") val links: String?,
    @JsonProperty("profile") val profile: Any?,
    @JsonProperty("settings") val settings: Any?,
    @JsonProperty("pinnedNoteIds") val pinnedNoteIds: List<String>?,
    @JsonProperty("pinnedNotes") val pinnedNotes: List<Note>?,
    @JsonProperty("twitter") val twitter: Any?,
    @JsonProperty("twoFactorEnabled") val twoFactorEnabled: Any?,
    @JsonProperty("isAdmin") val isAdmin: Boolean,
    @JsonProperty("avatarUrl") val avatarUrl: String?,
    @JsonProperty("bannerUrl") val bannerUrl: String?,
    @JsonProperty("avatarColor") val avatarColor: Any?,
    @JsonProperty("emojis") val emojis: List<EmojiProperty>?,
    @JsonProperty("isVerified") val isVerified: Boolean,
    @JsonProperty("isLocked") val isLocked: Boolean
    ): Serializable