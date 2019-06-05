package org.panta.misskey_nest.view_presenter.image_viewer

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ImagePageAdapter(fragmentManager: FragmentManager, private val imageUrlList: List<String>) : FragmentPagerAdapter(fragmentManager){

    override fun getCount(): Int {
        return imageUrlList.size
    }

    override fun getItem(p0: Int): Fragment {
        return ImageViewFragment.getInstance(imageUrlList[p0])
    }
}