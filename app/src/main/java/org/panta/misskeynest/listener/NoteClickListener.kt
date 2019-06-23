package org.panta.misskeynest.listener

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.constant.NoteType
import org.panta.misskeynest.dialog.ReactionDialog
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.repository.remote.NoteRepository
import org.panta.misskeynest.repository.remote.Reaction
import org.panta.misskeynest.util.copyToClipboad
import org.panta.misskeynest.view.EditNoteActivity
import org.panta.misskeynest.view.image_viewer.ImageViewerActivity
import org.panta.misskeynest.view.note_description.NoteDescriptionActivity
import org.panta.misskeynest.viewdata.NoteViewData

class NoteClickListener(private val context: Context, private val activity: Activity, private val connectionProperty: ConnectionProperty) :INoteClickListener {

    var onShowReactionDialog: (ReactionDialog)->Unit = {
        Log.d("NoteClickListener", "onShowReactionDialog is not init")
    }
    private val reactionRepository = Reaction(
        domain = connectionProperty.domain,
        authKey = connectionProperty.i
    )
    override fun onNoteClicked(note: Note) {
        Log.d("TimelineFragment", "Noteをクリックした :$note")
        val intent = Intent(context, NoteDescriptionActivity::class.java)
        intent.putExtra(NoteDescriptionActivity.NOTE_DESCRIPTION_NOTE_PROPERTY, note)
        context.startActivity(intent)
    }
    override fun onReplyButtonClicked(targetId: String?, note: Note?) {
        EditNoteActivity.startActivity(context, targetId, NoteType.REPLY)
    }

    override fun onReactionClicked(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?) {
        //mPresenter?.setReactionSelectedState(targetId, note, viewData, reactionType)
        reactionSelected(targetId, note, viewData, reactionType)
    }

    override fun onReNoteButtonClicked(targetId: String?, note: Note?) {
        EditNoteActivity.startActivity(context, targetId, NoteType.RE_NOTE)
    }

    override fun onDetailButtonClicked(note: Note) {
        val itemList = arrayListOf("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ")
        if(note.user?.id == connectionProperty.userPrimaryId){
            itemList.add("削除")
        }
        //val item = arrayOf<CharSequence>("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ（開発者向け）")

        val a = itemList.toTypedArray()

        AlertDialog.Builder(activity).apply{
            setTitle("詳細")
            setItems(itemList.toTypedArray()){ _, which->
                when(which){
                    0 -> copyToClipboad(context, note.text.toString())
                    1 -> copyToClipboad(context, "${connectionProperty.domain}/notes/${note.id}")
                    2,3 -> Toast.makeText(context, "未実装ですごめんなさい", Toast.LENGTH_SHORT)
                    4 ->{
                        AlertDialog.Builder(activity).apply{
                            val noteString = note.toString().replace(",","\n")
                            setMessage(noteString)
                            setPositiveButton(android.R.string.ok){i ,b->
                            }
                        }.show()
                    }
                    5 ->{
                        GlobalScope.launch {
                            val b = NoteRepository(connectionProperty).remove(note)
                            Handler(Looper.getMainLooper()).post{
                                val statusMsg = if(b) "成功しました" else "失敗しました"
                                Toast.makeText(context, statusMsg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            }
        }.show()


    }
    override fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>) {
        ImageViewerActivity.startActivity(context, clickedImageUrlCollection, clickedIndex)
    }

    override fun onMediaPlayClicked(fileProperty: FileProperty) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fileProperty.url)))
    }

    private fun reactionSelected(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?){
        Log.d("NoteClickListener", "targetId :$targetId, reactionType: $reactionType")
        if(targetId != null && note != null){
            when {
                viewData.toShowNote.myReaction != null -> deleteReaction(viewData.toShowNote.id)
                reactionType != null -> this.sendReaction(noteId = note.id, reactionType = reactionType, viewData = viewData)
                else -> showReactionSelectorView(targetId, viewData)
            }

        }
    }

    private fun showReactionSelectorView(targetId: String, viewData: NoteViewData) {
        Log.d("NoteClickListener", "showReactionSelectorViewが呼び出された")
        val reactionDialog = ReactionDialog.getInstance(targetId, object : ReactionDialog.CallBackListener{
            override fun callBack(noteId: String?, reactionParameter: String) {
                if(noteId != null){
                    Log.d("TimelineFragment", "成功した")
                    sendReaction(noteId = noteId, reactionType = reactionParameter, viewData = viewData)
                }
            }
        })
        onShowReactionDialog(reactionDialog)

    }

    private fun deleteReaction(noteId: String){
        reactionRepository.deleteReaction(noteId)
    }

    private fun sendReaction(noteId: String, viewData: NoteViewData, reactionType: String) {
        reactionRepository.sendReaction(noteId, reactionType){
            if(it){
                Log.d("NoteClickListener", "sendReaction成功したようだ")
                Handler(Looper.getMainLooper()).post{
                    Toast.makeText(context, "リアクションに成功しました", Toast.LENGTH_SHORT).show()
                }

            }else{
                //mView.onError("リアクションの送信に失敗した")
                Log.d("NoteClickListener", "sendReaction失敗しちゃった・・")
                Handler(Looper.getMainLooper()).post{
                    Toast.makeText(context, "リアクションに失敗しました。", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //mView.showUpdatedNote(viewData)


    }


}