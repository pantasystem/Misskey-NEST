package org.panta.misskey_nest.entity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown=true) data class MetaProperty(
    @JsonProperty("announcements") val announcements: List<Any?>?,
    @JsonProperty("bannerUrl") val bannerUrl: String?,
    @JsonProperty("cacheRemoteFiles") val cacheRemoteFiles: Boolean?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("disableGlobalTimeline") val disableGlobalTimeline: Boolean?,
    @JsonProperty("disableLocalTimeline") val disableLocalTimeline: Boolean?,
    @JsonProperty("disableRegistration") val disableRegistration: Boolean?,
    @JsonProperty("driveCapacityPerLocalUserMb") val driveCapacityPerLocalUserMb: Int?,
    @JsonProperty("driveCapacityPerRemoteUserMb") val driveCapacityPerRemoteUserMb: Int?,
    @JsonProperty("emojis") val emojis: List<EmojiProperty>?,
    @JsonProperty("enableDiscordIntegration") val enableDiscordIntegration: Boolean?,
    @JsonProperty("enableEmail") val enableEmail: Boolean?,
    @JsonProperty("enableEmojiReaction") val enableEmojiReaction: Boolean?,
    @JsonProperty("enableGithubIntegration") val enableGithubIntegration: Boolean?,
    @JsonProperty("enableRecaptcha") val enableRecaptcha: Boolean?,
    @JsonProperty("enableServiceWorker") val enableServiceWorker: Boolean?,
    @JsonProperty("enableTwitterIntegration") val enableTwitterIntegration: Boolean?,
    @JsonProperty("errorImageUrl") val errorImageUrl: String?,
    @JsonProperty("feedbackUrl") val feedbackUrl: String?,
    @JsonProperty("iconUrl") val iconUrl: String?,
    @JsonProperty("langs") val langs: List<Any?>?,
    @JsonProperty("machine") val machine: String?,
    @JsonProperty("maintainerEmail") val maintainerEmail: String?,
    @JsonProperty("maintainerName") val maintainerName: String?,
    @JsonProperty("mascotImageUrl") val mascotImageUrl: String?,
    @JsonProperty("maxNoteTextLength") val maxNoteTextLength: Int?,
    @JsonProperty("name") val name: String?,
    @JsonProperty("node") val node: String?,
    @JsonProperty("os") val os: String?,
    @JsonProperty("recaptchaSiteKey") val recaptchaSiteKey: String?,
    @JsonProperty("repositoryUrl") val repositoryUrl: String?,
    @JsonProperty("secure") val secure: Boolean?,
    @JsonProperty("swPublickey") val swPublickey: String?,
    @JsonProperty("ToSUrl") val toSUrl: String?,
    @JsonProperty("uri") val uri: String?,
    @JsonProperty("version") val version: String?
)