package org.panta.misskeynest.view_presenter.follow_follower

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_follow_follower.*
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.FollowsAdapter
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.FollowFollowerContract
import org.panta.misskeynest.repository.FollowFollowerRepository
import org.panta.misskeynest.view_data.FollowViewData
import java.lang.Exception

class FollowFollowerFragment : Fragment(), FollowFollowerContract.View,
    org.panta.misskeynest.adapter.FollowsAdapter.FollowAdapterListener{

    companion object{
        private const val USER_ID = "followFollowerFragment"
        private const val CONNECTION_INFO = "followFollowerConnectionInfo"
        private const val FOLLOW_FOLLOWER_TYPE = "FOLLOW_FOLLOWER_TYPE_INFO"
        fun getInstance(userId: String, type: FollowFollowerType,connectionInfo: ConnectionProperty): FollowFollowerFragment{
            val fragment = FollowFollowerFragment()
            val bundle = Bundle()
            bundle.putString(USER_ID, userId)
            bundle.putSerializable(CONNECTION_INFO, connectionInfo)
            bundle.putInt(FOLLOW_FOLLOWER_TYPE, type.ordinal)
            fragment.arguments = bundle
            return fragment
        }
    }

    override var mPresenter: FollowFollowerContract.Presenter? = null

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: org.panta.misskeynest.adapter.FollowsAdapter

    private lateinit var type: FollowFollowerType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_follow_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val info = args?.getSerializable(CONNECTION_INFO) as ConnectionProperty
        val userId = args.getString(USER_ID)!!
        val type = args.getInt(FOLLOW_FOLLOWER_TYPE, 0 )

        this.type = FollowFollowerType.getTypeFromOrdinal(type)
        val repository = FollowFollowerRepository(userId, this.type, info)
        mPresenter = FollowFollowerPresenter(this, repository, info)

        mLayoutManager = LinearLayoutManager(context)
        follow_follower_recycler_view.addOnScrollListener(listener)
        mPresenter?.getItems()

        follow_follower_refresh.setOnRefreshListener {
            mPresenter?.getNewItems()
        }

    }

    override fun showItems(list: List<FollowViewData>) {
        activity?.runOnUiThread{
            val adapter = org.panta.misskeynest.adapter.FollowsAdapter(list, this.type, this)
            mAdapter = adapter
            follow_follower_recycler_view.layoutManager = mLayoutManager
            follow_follower_recycler_view.adapter = adapter
        }
    }

    override fun showNewItems(list: List<FollowViewData>) {
        activity?.runOnUiThread{
            mAdapter.addAllFirst(list)
        }
    }

    override fun showOldItems(list: List<FollowViewData>) {
        activity?.runOnUiThread{
            mAdapter.addAllLast(list)
        }
    }

    override fun showError(e: Exception){

    }

    override fun stopRefreshing() {
        activity?.runOnUiThread{
            follow_follower_refresh?.isRefreshing = false
        }
    }

    override fun updateItem(item: FollowViewData) {
        activity?.runOnUiThread{
            mAdapter.updateItem(item)
        }
    }

    override fun removeItem(item: FollowViewData) {
        activity?.runOnUiThread{
            mAdapter.removeItem(item)
        }
    }

    override fun onFollowButtonClicked(item: FollowViewData) {
        mPresenter?.onFollowUnFollowButtonClicked(item)
    }

    override fun onUserClicked(user: User) {

    }

    private val listener = object : RecyclerView.OnScrollListener(){

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if( ! recyclerView.canScrollVertically(-1)){
                //先頭に来た場合
                refresh?.isEnabled = true
                Log.d("TimelineFragment", "先頭に来た")
            }
            if( ! recyclerView.canScrollVertically(1)){
                //最後に来た場合
                refresh?.isEnabled = false   //stopRefreshing関数を設けているがあえてこの形にしている
                mPresenter?.getOldItems()
            }
        }
    }

}