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
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.DriveAdapter
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.FolderProperty
import org.panta.misskeynest.filter.FileFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.repository.remote.FilePagingRepository
import org.panta.misskeynest.repository.remote.FileRepository
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.viewdata.DriveViewData

class FileFragment : Fragment(){

    companion object{
        const val EXTRA_FOLDER_PROPERTY = "org.panta.misskey.fragment.extra.folder.property"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file, container, false)
    }

    private lateinit var mPagingController: PagingController<FileProperty, DriveViewData.FileViewData>
    private lateinit var mAdapter: DriveAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this.context!!))
            .getConnectionInfo()

        val repository = FileRepository(connectionProperty!!)
        val folderProperty = arguments?.getSerializable(EXTRA_FOLDER_PROPERTY) as FolderProperty?
        mPagingController = PagingController<FileProperty, DriveViewData.FileViewData>(FilePagingRepository(repository, folderProperty?.id), mPagingErrorListener, FileFilter())

        mLayoutManager = LinearLayoutManager(this.context)
        list_view.layoutManager = mLayoutManager

        initLoad()
        list_view.addOnScrollListener(mScrollListener)
    }

    private fun initLoad(){
        mPagingController.init {
            Handler(Looper.getMainLooper()).post{
                mAdapter = DriveAdapter(it)
                list_view.adapter = mAdapter
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