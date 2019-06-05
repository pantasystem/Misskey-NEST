package org.panta.misskey_nest.view_presenter.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_notification.*
import org.panta.misskey_nest.R
import org.panta.misskey_nest.adapter.NotificationAdapter
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.interfaces.IOperationAdapter
import org.panta.misskey_nest.view_data.NotificationViewData

class NotificationFragment : Fragment(), NotificationContract.View{

    override var mPresenter: NotificationContract.Presenter? = null

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var notificationAdapter: IOperationAdapter<NotificationViewData>

    companion object{
        private const val CONNECTION_INFO = "NotificationFragmentConnectionInfo"
        fun getInstance(info: ConnectionProperty): NotificationFragment{
            val fragment = NotificationFragment()
            val bundle = Bundle()
            bundle.putSerializable(CONNECTION_INFO, info)
            fragment.arguments = bundle
            return fragment
        }
    }
    private var connectionInfo: ConnectionProperty? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        connectionInfo = arguments?.getSerializable(CONNECTION_INFO) as ConnectionProperty

        mPresenter = NotificationPresenter(this, connectionInfo!!)

        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLayoutManager = LinearLayoutManager(context)
        mPresenter?.initNotification()
        refresh.setOnRefreshListener{
            mPresenter?.getNewNotification()
        }

    }

    override fun stopRefreshing() {
        activity?.runOnUiThread{
            refresh?.isRefreshing = false
        }
    }

    override fun showOldNotification(list: List<NotificationViewData>) {
        activity?.runOnUiThread{
            notificationAdapter.addAllLast(list)
            stopRefreshing()
        }
    }

    override fun showNewNotification(list: List<NotificationViewData>) {
        activity?.runOnUiThread{
            notificationAdapter.addAllFirst(list)
            stopRefreshing()
            mPresenter?.markAllAsRead()
        }
    }

    override fun showInitNotification(list: List<NotificationViewData>) {
        activity?.runOnUiThread{

            notification_view?.layoutManager = mLayoutManager
            val adapter = NotificationAdapter(list)
            notification_view?.adapter = adapter
            notificationAdapter = adapter
            notification_view?.addOnScrollListener(listener)
            mPresenter?.markAllAsRead()
            stopRefreshing()
        }
    }

    override fun onError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    private val listener = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if( ! recyclerView.canScrollVertically(-1)){
                //先頭に来た場合
                refresh.isEnabled = true
                Log.d("TimelineFragment", "先頭に来た")
            }
            if( ! recyclerView.canScrollVertically(1)){
                //最後に来た場合
                refresh.isEnabled = false   //stopRefreshing関数を設けているがあえてこの形にしている
                mPresenter?.getOldNotification()
            }
        }
    }
}