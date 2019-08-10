package org.panta.misskeynest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
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
import org.panta.misskeynest.contract.TimelineContract
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.listener.NoteClickListener
import org.panta.misskeynest.listener.UserClickListener
import org.panta.misskeynest.presenter.TimelinePresenter
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.usecase.interactor.NoteCaptureUseCase
import org.panta.misskeynest.util.TimelineRepositoryFactory
import org.panta.misskeynest.viewdata.NoteViewData

class TimelineFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, TimelineContract.View {


    companion object{
        private const val CONNECTION_PROPERTY = "TimelineFragmentConnectionInfomation"
        private const val TIMELINE_TYPE = "TIMELINE_FRAGMENT_TIMELINE_TYPE"
        private const val USER_PROPERTY = "TIMELINE_FRAGMENT_USER_TIMELINE"
        private const val IS_MEDIA_ONLY = "IS_MEDIA_ONLY"
        private const val SEARCH_WORD = "SEARCH_WORD"
        private const val USER_PIN_NOTES = "TimelineFragmentUserPinNotesKey"

        fun getInstance(info: ConnectionProperty, type: TimelineTypeEnum, isMediaOnly: Boolean): TimelineFragment {
            return TimelineFragment().apply{
                val args = Bundle()
                args.putSerializable(CONNECTION_PROPERTY, info)
                args.putString(TIMELINE_TYPE, type.name)
                args.putBoolean(IS_MEDIA_ONLY, isMediaOnly)
                this.arguments = args
            }
        }

        fun getInstance(info: ConnectionProperty, user: User ,isMediaOnly: Boolean, isPin: Boolean): TimelineFragment {
            return TimelineFragment().apply{
                val args = Bundle()
                args.putSerializable(CONNECTION_PROPERTY, info)
                args.putSerializable(USER_PROPERTY, user)
                args.putBoolean(IS_MEDIA_ONLY, isMediaOnly)
                args.putBoolean(USER_PIN_NOTES, isPin)
                this.arguments = args
            }
        }

        fun getInstance(info: ConnectionProperty, word: String, isMediaOnly: Boolean): TimelineFragment {
            return TimelineFragment().apply{
                val args = Bundle()
                args.putSerializable(CONNECTION_PROPERTY, info)
                args.putString(SEARCH_WORD, word)
                args.putBoolean(IS_MEDIA_ONLY, isMediaOnly)
                this.arguments = args
            }
        }

    }

    override var mPresenter: TimelineContract.Presenter? = null

    private var connectionInfo: ConnectionProperty? = null

    //private var mTimelineType: TimelineTypeEnum = TimelineTypeEnum.HOME
    private val mLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(context)
    }
    private var mAdapter: TimelineAdapter? = null

    private lateinit var mNoteClickListener: NoteClickListener
    private lateinit var mUserClickListener: UserClickListener


    private var mNoteRepository: IItemRepository<Note>? = null

    private lateinit var mNoteCaptureUseCase: NoteCaptureUseCase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val args = arguments

        connectionInfo = args?.getSerializable(CONNECTION_PROPERTY) as ConnectionProperty
        val timelineType = args.getString(TIMELINE_TYPE)
        val timelineTypeEnum = if( timelineType == null ) null else TimelineTypeEnum.toEnum(timelineType)
        val connectionProperty = args.getSerializable(CONNECTION_PROPERTY) !!as ConnectionProperty
        val isMediaOnly = args.getBoolean(IS_MEDIA_ONLY)?: false
        val userProperty = args.getSerializable(USER_PROPERTY) as User?

        val isPin = args.getBoolean(USER_PIN_NOTES)?: false

        val searchWord = args.getString(SEARCH_WORD)

        val factory = TimelineRepositoryFactory(connectionProperty)
        mNoteRepository = when {
            (timelineTypeEnum == null) xor ( userProperty == null ) -> when {
                timelineTypeEnum != null -> factory.create(timelineTypeEnum)
                userProperty != null -> factory.create(userProperty, isMediaOnly, isPin)
                else -> throw IllegalArgumentException("不正な値です")
            }
            searchWord != null -> factory.create(searchWord, isMediaOnly)
            else -> throw IllegalArgumentException("不正な値です")
        }
        //val serializableRepository = SerializableRepository(context!!)

        mNoteCaptureUseCase= NoteCaptureUseCase(null, connectionInfo!!)
        mPresenter = TimelinePresenter(this,
            mNoteCaptureUseCase,
            mNoteRepository!!,
            connectionInfo!!,
            PersonalRepository(SharedPreferenceOperator(this.context!!))
        )
        //mNoteCaptureUseCase.start()

        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineView.addOnScrollListener(listener)
        //mPresenter?.initTimeline()
        mPresenter?.start()
        //mLayoutManager = LinearLayoutManager(context)

        refresh?.setOnRefreshListener(this)

        val dividerItemDecoration = DividerItemDecoration(timelineView.context, mLayoutManager.orientation)
        timelineView.addItemDecoration(dividerItemDecoration)

        initContentActionListener()

    }

    override fun onStart() {
        super.onStart()

        updateContentActionListener()

        //mPresenter?.start()
        mPresenter?.resume()
    }



    override fun onStop() {
        super.onStop()
        val position = mLayoutManager.findFirstVisibleItemPosition()
        val visibleFirstItem = mAdapter?.getItem(position)
        if(visibleFirstItem != null){
            mPresenter?.saveItem(visibleFirstItem)
        }

        mPresenter?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        mPresenter?.destroy()
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
            progress_bar?.visibility = View.GONE
            timelineView?.visibility = View.VISIBLE
            Log.d("TimelineFragment", "データの取得が完了した")


            mAdapter = TimelineAdapter(list)

            mAdapter?.addNoteClickListener(mNoteClickListener)
            mAdapter?.addUserClickListener(mUserClickListener)

            timelineView?.layoutManager = mLayoutManager
            timelineView?.adapter = mAdapter
            mNoteCaptureUseCase.mAdapterOperator = mAdapter

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

    override fun toString(): String {
        return "package org.panta.misskeynest.fragment.TimelineFragment"
    }

    private fun updateContentActionListener(){
        val isNoteClickable = PersonalRepository(SharedPreferenceOperator(this.context!!))
            .isNoteClickable
        mNoteClickListener.isNoteClickable = isNoteClickable
    }

    private fun initContentActionListener(){
        if(context != null && activity != null && connectionInfo != null){
            val isNoteClickable = PersonalRepository(SharedPreferenceOperator(this.context!!))
                .isNoteClickable

            mNoteClickListener = NoteClickListener(context!!, activity!!, connectionInfo!!, isNoteClickable)
            mNoteClickListener.onShowReactionDialog = {
                it.show(activity?.supportFragmentManager, "reaction_tag")
            }
            mUserClickListener = UserClickListener(context!!)
        }
    }

}