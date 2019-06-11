package org.panta.misskeynest.view_presenter.follow_follower

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.ConnectionProperty

class FollowPagerAdapter(fragmentManager: FragmentManager, private val connectionInfo: ConnectionProperty, private val userId: String) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("フォロー", "フォロワー")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")
        return tabTitles[position]
    }

    override fun getItem(p0: Int): Fragment? {
        val tmp = p0 % 2
        return when(tmp){
            0 ->{
                FollowFollowerFragment.getInstance(userId, FollowFollowerType.FOLLOWING, connectionInfo)
            }
            1 ->{
                FollowFollowerFragment.getInstance(userId, FollowFollowerType.FOLLOWER, connectionInfo)

            }

            else -> FollowFollowerFragment.getInstance(userId, FollowFollowerType.FOLLOWING, connectionInfo)


        }
    }

}