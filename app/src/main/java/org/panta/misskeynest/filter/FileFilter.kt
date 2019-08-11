package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.DriveViewData

class FileFilter : IItemFilter<FileProperty, DriveViewData.FileViewData>{
    override fun filter(items: List<FileProperty>): List<DriveViewData.FileViewData> {
        return items.map{
            filter(it)
        }
    }

    override fun filter(item: FileProperty): DriveViewData.FileViewData {
        return DriveViewData.FileViewData(id = item.id!!, isIgnore = false, fileProperty = item)
    }
}