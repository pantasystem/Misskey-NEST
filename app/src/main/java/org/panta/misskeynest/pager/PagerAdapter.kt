package org.panta.misskeynest.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.util.SparseArray
import org.panta.misskeynest.constant.TimelineTypeEnum
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.view.timeline.TimelineFragment

class PagerAdapter(fragmentManager: FragmentManager, private val connectionInfo: ConnectionProperty) : FragmentPagerAdapter(fragmentManager){

    private val fragmentList = SparseArray<TimelineFragment>()

    private val tabTitles = arrayOf<CharSequence>("Home", "Local", "Social","Global")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")

        //return tabTitles[position]
        //タブのタイトルは表示しないのでNULLを返す
        return null
    }

    override fun getItem(p0: Int): Fragment? {
        /*return when(p0){
            0 ->{
                TimelineFragment.getInstance(connectionInfo)

            }
            1 ->{
                Log.d("PagerAdapter", "Localを表示中")
                TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.LOCAL)
            }
            2 ->{
                Log.d("PagerAdapter", "SOCIALを表示中")
                TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.SOCIAL)
            }
            3 ->{
                Log.d("PagerAdapter", "Globalを表示中")
                TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.GLOBAL)
            }

            else -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.LOCAL)


        }*/
        /*val factory = TimelineRepositoryFactory(connectionInfo)
        val repository = when(p0){
            0 -> factory.create(TimelineTypeEnum.HOME)
            1 -> factory.create(TimelineTypeEnum.LOCAL)
            2 -> factory.create(TimelineTypeEnum.SOCIAL)
            3 -> factory.create(TimelineTypeEnum.GLOBAL)
            else -> throw IllegalAccessException("おかしな呼び出し")

        }!!
        val fragment = TimelineFragment.getInstance(connectionInfo)
        fragment.mNoteRepository= repository
        fragmentList.put(p0, fragment)*/
        return when(p0){
            0 -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.HOME, false)
            1 -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.LOCAL, false)
            2 -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.SOCIAL, false)
            3 -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.GLOBAL, false)
            else -> throw IllegalArgumentException("異常な呼び出し")
        }

    }

    fun getFragment(position: Int): TimelineFragment{
        return fragmentList.get(position)
    }


}