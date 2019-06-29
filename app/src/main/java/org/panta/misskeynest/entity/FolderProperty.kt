package org.panta.misskeynest.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.panta.misskeynest.constant.REMOTE_DATE_FORMAT
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true) data class FolderProperty(
    val id: String,
    @JsonFormat(pattern = REMOTE_DATE_FORMAT) val createdAt:Date,
    val name: String,
    val foldersCount: Int,
    val filesCount: Int,
    val parentId: String?,
    val parent: FolderProperty?
)