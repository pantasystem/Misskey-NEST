package org.panta.misskeynest.repository

import org.panta.misskeynest.entity.FileProperty
import java.io.File
import java.net.URL

interface IFileRepository {
    fun getItems(limit: Int?, sinceId: String?, untilId: String?, folderId: String?, type: String?)
    fun create(folderId: String?, isSensitive: Boolean, force: Boolean, file: File): FileProperty?
    fun delete(id: String): Boolean
    fun show(id: String, url: URL): FileProperty?
    fun update(fileId: String, folderId: String?, fileName: String?, isSensitive: Boolean?): Boolean

}