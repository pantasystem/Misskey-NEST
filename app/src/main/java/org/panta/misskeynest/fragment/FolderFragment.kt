package org.panta.misskeynest.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_file.*
import org.panta.misskeynest.DriveActivity
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.DriveAdapter
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.filter.FolderFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.ResetFragment
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.repository.remote.FolderPagingRepository
import org.panta.misskeynest.repository.remote.FolderRepository
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.viewdata.DriveViewData

class FolderFragment : Fragment(), ResetFragment{

    companion object{
        const val EXTRA_FOLDER_PROPERTY = "org.panta.misskeynest.fragment.FolderFragment.extra_folder_property"

        fun newInstance(property: DriveViewData.FolderViewData? = null): FolderFragment{
            val fragment = FolderFragment()

            fragment.arguments = Bundle().apply{
                if(property != null){
                    putSerializable(FileFragment.EXTRA_FOLDER_PROPERTY, property)
                }
            }
            return fragment
        }
    }

    private lateinit var mPagingController: PagingController<FolderProperty, DriveViewData.FolderViewData>
    private lateinit var mAdapter: DriveAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_folder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this.context!!))
            .getConnectionInfo()

        val repository = FolderRepository(connectionProperty!!)
        val folderProperty = arguments?.getSerializable(FileFragment.EXTRA_FOLDER_PROPERTY) as FolderProperty?
        mPagingController = PagingController<FolderProperty, DriveViewData.FolderViewData>(FolderPagingRepository(repository, folderProperty?.id), mPagingErrorListener, FolderFilter())

        mLayoutManager = LinearLayoutManager(this.context)
        list_view.layoutManager = mLayoutManager

        initLoad()
        list_view.addOnScrollListener(mScrollListener)
        refresh.setOnRefreshListener {
            loadAfter()
        }

    }

    override fun reset(id: String?){
        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this.context!!))
            .getConnectionInfo()

        val repository = FolderRepository(connectionProperty!!)
        mPagingController = PagingController<FolderProperty, DriveViewData.FolderViewData>(FolderPagingRepository(repository, id), mPagingErrorListener, FolderFilter())
        initLoad()
    }

    private fun initLoad(){
        mPagingController.init {
            Handler(Looper.getMainLooper()).post{
                mAdapter = DriveAdapter(it)
                list_view.adapter = mAdapter
                mAdapter.itemClickListener = activity as DriveActivity
                refresh.isRefreshing = false
            }
        }
    }

    private fun loadBefore(){
        mPagingController.getOldItems {
            Handler(Looper.getMainLooper()).post{
                mAdapter.addAllLast(it)
                refresh.isRefreshing = false
            }
        }
    }

    private fun loadAfter(){
        mPagingController.getNewItems {
            Handler(Looper.getMainLooper()).post{
                mAdapter.addAllFirst(it)
                refresh.isRefreshing = false
            }
        }
    }


    private val mPagingErrorListener = object : ErrorCallBackListener{
        override fun callBack(e: Exception) {
            refresh.isRefreshing = false
        }
    }

    private val mScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if( ! recyclerView.canScrollVertically(-1)){
                //先頭に来た場合
                refresh.isEnabled = true
            }
            if( ! recyclerView.canScrollVertically(1)){
                //最後に来た場合
                refresh.isEnabled = false   //stopRefreshing関数を設けているがあえてこの形にしている
                loadBefore()
            }
        }
    }

}