package org.panta.misskeynest.view.timeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.TimelineAdapter
import org.panta.misskeynest.constant.TimelineTypeEnum
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.interfaces.IBindScrollPosition
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.listener.NoteClickListener
import org.panta.misskeynest.listener.UserClickListener
import org.panta.misskeynest.usecase.ObservationNote
import org.panta.misskeynest.viewdata.NoteViewData

class TimelineFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, TimelineContract.View,
    /*NoteClickListener,*/ /*UserClickListener,*/ IBindScrollPosition{


    companion object{
        private const val CONNECTION_INFOMATION = "TimelineFragmentConnectionInfomation"
        private const val TIMELINE_TYPE = "TIMELINE_FRAGMENT_TIMELINE_TYPE"
        private const val USER_ID = "TIMELINE_FRAGMENT_USER_TIMELINE"
        private const val IS_MEDIA_ONLY = "IS_MEDIA_ONLY"

        fun getInstance(info: ConnectionProperty/*, type: TimelineTypeEnum, userId: String? = null, isMediaOnly: Boolean = false*/): TimelineFragment{
            return TimelineFragment().apply{
                val args = Bundle()
                args.putSerializable(CONNECTION_INFOMATION, info)
                this.arguments = args
            }
        }

    }

    override var mPresenter: TimelineContract.Presenter? = null

    private var connectionInfo: ConnectionProperty? = null

    private var mTimelineType: TimelineTypeEnum = TimelineTypeEnum.HOME
    private val mLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(context)
    }
    private var mAdapter: TimelineAdapter? = null

    private lateinit var mNoteClickListener: NoteClickListener
    private lateinit var mUserClickListener: UserClickListener

    var mNoteRepository: IItemRepository<NoteViewData>? = null
        set(value) {
            field = value
            if(value != null && connectionInfo != null){
                mPresenter = TimelinePresenter(this, mNoteRepository!!, connectionInfo!!)
                mPresenter?.initTimeline()
            }else{
                Log.d(tag, if(value == null) "NULLだなんでだ！！！" else "重要な情報が来ない")
            }
        }

    var mObservationNote: ObservationNote? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val args = arguments

        connectionInfo = args?.getSerializable(CONNECTION_INFOMATION) as ConnectionProperty
        val timelineType = args.getString(TIMELINE_TYPE)
        //isMediaOnly = args.getBoolean(IS_MEDIA_ONLY)
        //userId = args.getString(USER_ID)

        if(timelineType != null){
            mTimelineType = TimelineTypeEnum.toEnum(timelineType)
        }

        if(mNoteRepository != null){
            mPresenter = TimelinePresenter(this, mNoteRepository!!, connectionInfo!!)
        }else{
            Log.d(tag, "プレゼンターを作成することができなかった")
        }

        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineView.addOnScrollListener(listener)
        mPresenter?.initTimeline()
        //mLayoutManager = LinearLayoutManager(context)

        refresh?.setOnRefreshListener(this)

        if(context != null && activity != null && connectionInfo != null){
            mNoteClickListener = NoteClickListener(context!!, activity!!, connectionInfo!!)
            mNoteClickListener.onShowReactionDialog = {
                it.show(activity?.supportFragmentManager, "reaction_tag")
            }
            mUserClickListener = UserClickListener(context!!)
        }

    }


    override fun onRefresh() {
        mPresenter?.getNewTimeline()
    }

    override fun stopRefreshing() {
        activity?.runOnUiThread{
            refresh?.isRefreshing = false
        }
    }



    override fun showInitTimeline(list: List<NoteViewData>) {
        activity?.runOnUiThread {
            load_icon?.visibility = View.GONE
            timelineView?.visibility = View.VISIBLE
            Log.d("TimelineFragment", "データの取得が完了した")


            mAdapter = TimelineAdapter(context!!, list)

            mAdapter?.addNoteClickListener(mNoteClickListener)
            mAdapter?.addUserClickListener(mUserClickListener)


            timelineView?.layoutManager = mLayoutManager
            timelineView?.adapter = mAdapter

            mObservationNote = ObservationNote(mAdapter!!, this, connectionInfo!!)
            stopRefreshing()

        }
    }



    override fun showNewTimeline(list: List<NoteViewData>) {
        mPresenter?.onRefresh()
        activity?.runOnUiThread {
            stopRefreshing()
            mAdapter?.addAllFirst(list)
        }

    }

    override fun showOldTimeline(list: List<NoteViewData>) {
        activity?.runOnUiThread{
            refresh?.isRefreshing = false
            mAdapter?.addAllLast(list)
        }

    }

    override fun showUpdatedNote(noteViewData: NoteViewData) {
        activity?.runOnUiThread {
            mAdapter?.updateItem(noteViewData)
            Log.d("TimelineFragment", "更新後のデータ ${noteViewData.toShowNote}, ${noteViewData.reactionCountPairList}")

        }

    }



    override fun onError(errorMsg: String) {
        Log.w("TimelineFragment", "エラー発生 message$errorMsg")
    }


    override fun bindFindItemCount(): Int? {
        return timelineView?.childCount
    }

    override fun bindFirstVisibleItemPosition(): Int? {
        return mLayoutManager.findFirstVisibleItemPosition()
    }

    override fun bindTotalItemCount(): Int? {
        return mLayoutManager.itemCount
    }

    /*override fun pickViewData(index: Int): NoteViewData? {
        return mAdapter?.getItem(index)
    }

    override fun pickViewData(viewData: NoteViewData): NoteViewData? {
        return mAdapter?.getItem(viewData)
    }*/


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
                mPresenter?.getOldTimeline()
            }
        }
    }

}