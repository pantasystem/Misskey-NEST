package org.panta.misskeynest.viewdata

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.interfaces.ID

sealed class DriveViewData : ID{
    data class FolderViewData(override val id: String, override val isIgnore: Boolean, val folder: FolderProperty): DriveViewData()
    data class FileViewData(override val id: String, override val isIgnore: Boolean, val fileProperty: FileProperty): DriveViewData()
}