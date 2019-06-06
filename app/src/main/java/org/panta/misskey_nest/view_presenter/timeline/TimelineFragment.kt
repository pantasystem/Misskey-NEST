package org.panta.misskey_nest.view_presenter.timeline

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
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
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.panta.misskey_nest.R
import org.panta.misskey_nest.adapter.TimelineAdapter
import org.panta.misskey_nest.constant.NoteType
import org.panta.misskey_nest.constant.TimelineTypeEnum
import org.panta.misskey_nest.dialog.ReactionDialog
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.entity.FileProperty
import org.panta.misskey_nest.entity.Note
import org.panta.misskey_nest.entity.User
import org.panta.misskey_nest.interfaces.IBindScrollPosition
import org.panta.misskey_nest.interfaces.IItemRepository
import org.panta.misskey_nest.interfaces.NoteClickListener
import org.panta.misskey_nest.interfaces.UserClickListener
import org.panta.misskey_nest.repository.*
import org.panta.misskey_nest.util.copyToClipboad
import org.panta.misskey_nest.view_data.NoteViewData
import org.panta.misskey_nest.view_presenter.image_viewer.ImageViewerActivity
import org.panta.misskey_nest.view_presenter.note_description.NoteDescriptionActivity
import org.panta.misskey_nest.view_presenter.note_editor.EditNoteActivity
import org.panta.misskey_nest.view_presenter.user.UserActivity
import java.io.File
import java.net.URL

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

    private val reactionRequestCode = 23457

    //private var instanceDomain: String? = null
    //private var i: String? = null
    private var connectionInfo: ConnectionProperty? = null

    private var mTimelineType: TimelineTypeEnum = TimelineTypeEnum.HOME
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: TimelineAdapter? = null

    //private var isMediaOnly: Boolean? = null
    //private var userId: String? = null
    var customEmojiList: List<File>? = null
        set(value){
            field = value

        }

    var mNoteRepository: IItemRepository<NoteViewData>? = null
        set(value) {
            field = value
            if(value != null && connectionInfo != null){
                mPresenter = TimelinePresenter(this, this, value, connectionInfo!!)
                mPresenter?.initTimeline()
            }else{
                Log.d(tag, if(value == null) "NULLだなんでだ！！！" else "重要な情報が来ない")
            }
        }



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
            mPresenter = TimelinePresenter(this, this, mNoteRepository!!, connectionInfo!!)
        }else{
            Log.d(tag, "プレゼンターを作成することができなかった")
        }

        customEmojiList = context?.fileList()?.toList()?.map{
            File(it)
        }
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineView.addOnScrollListener(listener)
        mPresenter?.initTimeline()
        mLayoutManager = LinearLayoutManager(context)

        refresh?.setOnRefreshListener(this)

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
            mAdapter?.reactionIconFileList = customEmojiList
            mAdapter?.addNoteClickListener(noteClickListener)
            mAdapter?.addUserClickListener(userClickListener)


            timelineView?.layoutManager = mLayoutManager
            timelineView?.adapter = mAdapter

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



    override fun showReactionSelectorView(targetId: String, viewData: NoteViewData) {
        val reactionDialog = ReactionDialog.getInstance(targetId, object : ReactionDialog.CallBackListener{
            override fun callBack(noteId: String?, reactionParameter: String) {
                if(noteId != null){
                    Log.d("TimelineFragment", "成功した")
                    mPresenter?.sendReaction(noteId = noteId, reactionType = reactionParameter, viewData = viewData)
                }
            }
        })

        activity?.runOnUiThread{
            reactionDialog.setTargetFragment(parentFragment, reactionRequestCode)
            reactionDialog.show(activity?.supportFragmentManager, "reaction_tag")
        }

    }

    override fun bindFindItemCount(): Int? {
        return timelineView?.childCount
    }

    override fun bindFirstVisibleItemPosition(): Int? {
        return mLayoutManager?.findFirstVisibleItemPosition()
    }

    override fun bindTotalItemCount(): Int? {
        return mLayoutManager?.itemCount
    }

    override fun pickViewData(index: Int): NoteViewData? {
        return mAdapter?.getItem(index)
    }

    override fun pickViewData(viewData: NoteViewData): NoteViewData? {
        return mAdapter?.getItem(viewData)
    }


    private val userClickListener = object: UserClickListener{
        override fun onClickedUser(user: User) {
            UserActivity.startActivity(context!!, user)
        }
    }

    private val noteClickListener = object: NoteClickListener{
        override fun onNoteClicked(targetId: String?, note: Note?) {
            Log.d("TimelineFragment", "Noteをクリックした")
            val intent = Intent(context, NoteDescriptionActivity::class.java)
            intent.putExtra(NoteDescriptionActivity.NOTE_DESCRIPTION_NOTE_PROPERTY, note)
            startActivity(intent)
        }
        override fun onReplyButtonClicked(targetId: String?, note: Note?) {
            EditNoteActivity.startActivity(context!!, targetId, NoteType.REPLY)
        }

        override fun onReactionClicked(targetId: String?, note: Note?, viewData: NoteViewData,reactionType: String?) {
            mPresenter?.setReactionSelectedState(targetId, note, viewData, reactionType)
        }

        override fun onReNoteButtonClicked(targetId: String?, note: Note?) {
            EditNoteActivity.startActivity(context!!, targetId, NoteType.RE_NOTE)
        }

        override fun onDescriptionButtonClicked(targetId: String?, note: Note?) {
            val item = arrayOf<CharSequence>("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ（開発者向け）")

            AlertDialog.Builder(activity).apply{
                setTitle("詳細")
                setItems(item){ dialog, which->
                    when(which){
                        0 -> copyToClipboad(context, note?.text.toString())
                        1 -> copyToClipboad(context, note?.url.toString())
                        2,3 -> Toast.makeText(context, "未実装ですごめんなさい", Toast.LENGTH_SHORT)
                        4 ->{
                            AlertDialog.Builder(activity).apply{
                                if(note is Note){
                                    val noteString = note.toString().replace(",","\n")
                                    setMessage(noteString)
                                }
                                setPositiveButton(android.R.string.ok){i ,b->
                                }
                            }.show()
                        }
                    }

                }
            }.show()


        }
        override fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>) {
            ImageViewerActivity.startActivity(context, clickedImageUrlCollection, clickedIndex)
        }

        override fun onMediaPlayClicked(fileProperty: FileProperty) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fileProperty.url)))
        }


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

}