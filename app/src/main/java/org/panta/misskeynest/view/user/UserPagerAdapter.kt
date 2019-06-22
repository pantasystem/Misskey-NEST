package org.panta.misskeynest.view.user

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.view.timeline.TimelineFragment

class UserPagerAdapter(fragmentManager: FragmentManager, private val user: User, private val connectionInfo: ConnectionProperty) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("タイムライン","メディア")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")
        return tabTitles[position]
    }

    override fun getItem(p0: Int): Fragment? {

        return when( p0 ){
            0 -> TimelineFragment.getInstance(connectionInfo, user, false)
            1 -> TimelineFragment.getInstance(connectionInfo, user, true)
            else -> throw IllegalAccessException("不正な値")
        }

    }

}