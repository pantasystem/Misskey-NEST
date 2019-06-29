package org.panta.misskeynest.repository

import org.panta.misskeynest.entity.FolderProperty

interface IFolderRepository {
    fun getItems(limit: Int, sinceId: String?, untilId: String?, folderId: String?): List<FolderProperty>?
    fun create(name: String, parentId: String?): Boolean
    fun delete(folderId: String): Boolean
    fun find(name: String, parentId: String?): FolderProperty?
    fun show(folderId: String): FolderProperty?
    fun update(folderId: String, name: String, parentId: String?): Boolean
}