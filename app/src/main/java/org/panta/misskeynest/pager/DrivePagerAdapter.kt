package org.panta.misskeynest.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.fragment.FileFragment
import org.panta.misskeynest.fragment.FolderFragment

class DrivePagerAdapter(fm: FragmentManager, private val folderProperty: FolderProperty?) : FragmentPagerAdapter(fm){

    private val pageList = arrayOf("ファイル", "フォルダ")
    override fun getCount(): Int {
        return pageList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return pageList[position]
    }

    override fun getItem(p0: Int): Fragment {
        return when(p0){
            0 ->{
               FileFragment()
            }
            1 ->{
                FolderFragment()
            }
            else -> throw IllegalArgumentException("数がおかしい")
        }
    }
}