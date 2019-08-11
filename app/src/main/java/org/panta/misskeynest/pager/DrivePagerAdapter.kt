package org.panta.misskeynest.pager

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.panta.misskeynest.fragment.FileFragment
import org.panta.misskeynest.fragment.FolderFragment
import org.panta.misskeynest.interfaces.ResetFragment
import org.panta.misskeynest.viewdata.DriveViewData

class DrivePagerAdapter(fm: FragmentManager, private val folderProperty: DriveViewData.FolderViewData?) : FragmentPagerAdapter(fm){

    private val pageList = arrayOf("ファイル", "フォルダ")

    @SuppressLint("UseSparseArrays")
    private val mFragmentMap = HashMap<Int, ResetFragment>()

    override fun getCount(): Int {
        return pageList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return pageList[position]
    }


    override fun getItem(p0: Int): Fragment {
        return when(p0){
            0 ->{
                val fragment = FileFragment.newInstance(folderProperty)

                mFragmentMap[p0] = fragment
                fragment
            }
            1 ->{
                val fragment = FolderFragment.newInstance(folderProperty)
                mFragmentMap[p0] = fragment
                fragment
            }
            else -> throw IllegalArgumentException("数がおかしい")
        }
        //mFragmentMap[p0] = fragment
        //return fragment
    }

    fun refresh(property: DriveViewData.FolderViewData){
        mFragmentMap.forEach{
            it.value.reset(property)
        }
    }
}