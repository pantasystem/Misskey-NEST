package org.panta.misskeynest.view_presenter.notification

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
import kotlinx.android.synthetic.main.fragment_notification.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.NotificationAdapter
import org.panta.misskeynest.constant.NoteType
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.interfaces.IOperationAdapter
import org.panta.misskeynest.interfaces.IUserClickListener
import org.panta.misskeynest.util.copyToClipboad
import org.panta.misskeynest.view_data.NoteViewData
import org.panta.misskeynest.view_data.NotificationViewData
import org.panta.misskeynest.view_presenter.image_viewer.ImageViewerActivity
import org.panta.misskeynest.view_presenter.note_description.NoteDescriptionActivity
import org.panta.misskeynest.view_presenter.note_editor.EditNoteActivity
import org.panta.misskeynest.view_presenter.user.UserActivity

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
            adapter.noteClickListener = noteClickListener
            adapter.userClickListener = userClickListener

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


    //TODO TimelineFragmentからコピーしてきた必ず共通化する
    private val userClickListener = object: IUserClickListener {
        override fun onClickedUser(user: User) {
            UserActivity.startActivity(context!!, user)
        }
    }

    private val noteClickListener = object: INoteClickListener {
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
            //mPresenter?.setReactionSelectedState(targetId, note, viewData, reactionType)
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



}