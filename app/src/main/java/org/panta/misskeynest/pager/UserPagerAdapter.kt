package org.panta.misskeynest.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.fragment.TimelineFragment

class UserPagerAdapter(fragmentManager: FragmentManager, private val user: User, private val connectionInfo: ConnectionProperty) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("投稿", "ピン", "メディア")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")
        return tabTitles[position]
    }

    override fun getItem(p0: Int): Fragment? {

        return when( p0 ){
            0 -> TimelineFragment.getInstance(connectionInfo, user, isMediaOnly = false, isPin = false)
            1 -> TimelineFragment.getInstance(connectionInfo, user, isMediaOnly = false, isPin = true)
            2 -> TimelineFragment.getInstance(connectionInfo, user, isMediaOnly = true, isPin = false)
            else -> throw IllegalAccessException("不正な値")
        }

    }

}