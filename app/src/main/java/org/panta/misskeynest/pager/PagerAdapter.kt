package org.panta.misskeynest.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.util.SparseArray
import org.panta.misskeynest.constant.TimelineTypeEnum
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.fragment.NotificationFragment
import org.panta.misskeynest.fragment.TimelineFragment

class PagerAdapter(fragmentManager: FragmentManager, private val connectionInfo: ConnectionProperty, private val columnList: Array<String>) : FragmentPagerAdapter(fragmentManager){

    private val fragmentList = SparseArray<Fragment>()

    //private val tabTitles = arrayOf<CharSequence>("Home", "Local", "Social","Global")
    override fun getCount(): Int {
        return columnList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")

        //return tabTitles[position]
        //タブのタイトルは表示しないのでNULLを返す
        return null
    }

    override fun getItem(p0: Int): Fragment? {

        val title = columnList[p0].toString()
        val fragment: Fragment =  when(title.toLowerCase()){
            "home" -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.HOME, false)
            "local" -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.LOCAL, false)
            "social" -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.SOCIAL, false)
            "global" -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.GLOBAL, false)
            "notification" -> NotificationFragment.getInstance(connectionInfo)
            else -> throw IllegalArgumentException("異常な呼び出し")
        }
        fragmentList.put(p0, fragment)
        return fragment

    }

    fun getFragment(position: Int): Fragment {
        return fragmentList.get(position)
    }


}