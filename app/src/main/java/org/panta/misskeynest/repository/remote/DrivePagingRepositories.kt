package org.panta.misskeynest.repository.remote

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.repository.IItemRepository

class FolderPagingRepository : IItemRepository<FolderProperty>{
    override fun getItems(): List<FolderProperty>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseSinceId(sinceId: String): List<FolderProperty>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseUntilId(untilId: String): List<FolderProperty>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class FilePagingRepository : IItemRepository<FileProperty>{
    override fun getItems(): List<FileProperty>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseSinceId(sinceId: String): List<FileProperty>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseUntilId(untilId: String): List<FileProperty>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}