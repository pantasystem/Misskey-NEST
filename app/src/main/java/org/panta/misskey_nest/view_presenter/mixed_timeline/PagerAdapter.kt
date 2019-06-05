package org.panta.misskey_nest.view_presenter.mixed_timeline

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import org.panta.misskey_nest.constant.TimelineTypeEnum
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.util.PopularTimelineRepositoryFactory
import org.panta.misskey_nest.view_presenter.timeline.TimelineFragment

class PagerAdapter(fragmentManager: FragmentManager, private val connectionInfo: ConnectionProperty) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("Home", "Local", "Social","Global")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")

        return tabTitles[position]
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
        val factory = PopularTimelineRepositoryFactory(connectionInfo)
        val repository = when(p0){
            0 -> factory.create(TimelineTypeEnum.HOME)
            1 -> factory.create(TimelineTypeEnum.LOCAL)
            2 -> factory.create(TimelineTypeEnum.SOCIAL)
            3 -> factory.create(TimelineTypeEnum.GLOBAL)
            else -> throw IllegalAccessException("おかしな呼び出し")

        }!!
        val fragment = TimelineFragment.getInstance(connectionInfo)
        fragment.mNoteRepository= repository
        return fragment
    }


}