package org.panta.misskeynest.filter

import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.DriveViewData

class FolderFilter : IItemFilter<FolderProperty, DriveViewData.FolderViewData>{
    override fun filter(item: FolderProperty): DriveViewData.FolderViewData {
        return DriveViewData.FolderViewData(item.id, false, item)
    }

    override fun filter(items: List<FolderProperty>): List<DriveViewData.FolderViewData> {
        return items.map{
            filter(it)
        }
    }
}