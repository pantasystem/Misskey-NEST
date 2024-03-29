package org.panta.misskeynest.view.user

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.repository.LocalTimeline
import org.panta.misskeynest.repository.UserTimeline
import org.panta.misskeynest.view.timeline.TimelineFragment

class UserPagerAdapter(fragmentManager: FragmentManager, private val userId: String, private val connectionInfo: ConnectionProperty) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("概要", "タイムライン","メディア")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")
        return tabTitles[position]
    }

    override fun getItem(p0: Int): Fragment? {

        val repository = when(p0){
            0 -> LocalTimeline(domain = connectionInfo.domain, authKey = connectionInfo.i)
            1 -> UserTimeline(domain = connectionInfo.domain, userId = userId)
            2 -> UserTimeline(domain = connectionInfo.domain, userId = userId, isMediaOnly = true)
            else -> throw IllegalAccessException("想定していないタイムラインを要求している")
        }
        val fragment = TimelineFragment.getInstance(connectionInfo)
        fragment.mNoteRepository = repository
        return fragment

    }

}