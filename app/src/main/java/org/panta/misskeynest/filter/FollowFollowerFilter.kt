package org.panta.misskeynest.filter

import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.viewdata.FollowViewData

class FollowFollowerFilter : IItemFilter<FollowViewData, FollowViewData>{
    override fun filter(items: List<FollowViewData>): List<FollowViewData> {
        return items
    }

    override fun filter(item: FollowViewData): FollowViewData {
        return item
    }
}