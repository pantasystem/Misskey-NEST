package org.panta.misskeynest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_drive.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.DriveIndexAdapter
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.pager.DrivePagerAdapter
import org.panta.misskeynest.viewdata.DriveViewData
import java.util.*

class DriveFragment : Fragment(){

    companion object{
        const val EXTRA_FOLDER_VIEW_DATA= "org.panta.misskeynest.fragment.DriveFragment.extra_folder_property"

        fun newInstance(folderViewData: DriveViewData.FolderViewData?): DriveFragment{
            val fragment = DriveFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(EXTRA_FOLDER_VIEW_DATA, folderViewData)
            }
            return fragment

        }
    }

    private var pagerAdapter: DrivePagerAdapter? = null

    private val currentHistoryQueue = ArrayDeque<DriveViewData.FolderViewData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderProperty = arguments?.getSerializable(EXTRA_FOLDER_VIEW_DATA) as DriveViewData.FolderViewData?
        pagerAdapter = DrivePagerAdapter(fragmentManager!!, folderProperty)
        drive_pager.adapter = pagerAdapter
        drive_pager.offscreenPageLimit = 2
        drive_tab.setupWithViewPager(drive_pager)
        //currentHistoryQueue.add(null)
    }

    fun reset(viewData: DriveViewData.FolderViewData){
        pagerAdapter?.refresh(viewData.id)
        currentHistoryQueue.add(viewData)
        setIndexListView(currentHistoryQueue)
    }

    fun goBack(): Boolean{
        return try{
            val item = currentHistoryQueue.removeLast()
            setIndexListView(currentHistoryQueue)
            Log.d("DriveFragment", "取り出されたデータ $item")
            if(item == null){
                false
            }else{
                pagerAdapter?.refresh(item.folder.parentId)
                true
            }
        }catch(e: NoSuchElementException){
            Log.d("DriveFragment", "Rootにたどり着いた")
            false
        }

    }

    private fun setIndexListView(queue: Queue<DriveViewData.FolderViewData>){
        val list = queue.toList()
        val adapter = DriveIndexAdapter(list)
        adapter.itemClickListener = mDriveIndexListItemClickListener
        val lm = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        drive_index_list_view.layoutManager = lm
        drive_index_list_view.adapter = adapter
    }

    private val mDriveIndexListItemClickListener = object : ItemClickListener<DriveViewData.FolderViewData>{
        override fun onClick(item: DriveViewData.FolderViewData) {
            while(true){
                try{
                    val listItem = currentHistoryQueue.removeLast()
                    if(listItem.id == item.id){
                        //pagerAdapter?.refresh(listItem.id)
                        reset(listItem)
                        setIndexListView(currentHistoryQueue)
                        break
                    }
                }catch(e: Exception){
                    break
                }
            }
        }
    }

    override fun toString(): String{
        return "DriveFragment"
    }
}