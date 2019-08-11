package org.panta.misskeynest.repository.remote

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.repository.IFileRepository
import org.panta.misskeynest.repository.IFolderRepository
import org.panta.misskeynest.repository.IItemRepository

class FolderPagingRepository(private val folderRepository: IFolderRepository, private val folderId: String?) : IItemRepository<FolderProperty>{
    override fun getItems(): List<FolderProperty>? {
        return folderRepository.getItems(20, null, null, folderId)
    }

    override fun getItemsUseSinceId(sinceId: String): List<FolderProperty>? {
        return folderRepository.getItems(20, sinceId, null, folderId)?.asReversed()
    }

    override fun getItemsUseUntilId(untilId: String): List<FolderProperty>? {
        return folderRepository.getItems(20, null, untilId, folderId)
    }
}

class FilePagingRepository(private val fileRepository: IFileRepository, private val folderId: String?) : IItemRepository<FileProperty>{
    override fun getItems(): List<FileProperty>? {
        return fileRepository.getItems(20, null, null, folderId, null)
    }

    override fun getItemsUseSinceId(sinceId: String): List<FileProperty>? {
        return fileRepository.getItems(20, sinceId, null, folderId, null)?.asReversed()
    }

    override fun getItemsUseUntilId(untilId: String): List<FileProperty>? {
        return fileRepository.getItems(20, null, untilId, folderId, null)
    }
}