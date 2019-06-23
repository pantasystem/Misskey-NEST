package org.panta.misskeynest.listener

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.EditNoteActivity
import org.panta.misskeynest.ImageViewerActivity
import org.panta.misskeynest.NoteDescriptionActivity
import org.panta.misskeynest.constant.NoteType
import org.panta.misskeynest.dialog.DetailDialog
import org.panta.misskeynest.dialog.ReactionDialog
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.INoteClickListener
import org.panta.misskeynest.repository.remote.ReactionRepository
import org.panta.misskeynest.viewdata.NoteViewData

class NoteClickListener(private val context: Context, private val activity: Activity, private val connectionProperty: ConnectionProperty) :INoteClickListener {

    var onShowReactionDialog: (DialogFragment)->Unit = {
        Log.d("NoteClickListener", "onShowReactionDialog is not init")
    }
    private val reactionRepository = ReactionRepository(
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
        context.startActivity(EditNoteActivity.getIntent(context, targetId, NoteType.REPLY))
    }

    override fun onReactionClicked(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?) {
        //mPresenter?.setReactionSelectedState(targetId, note, viewData, reactionType)
        reactionSelected(viewData, reactionType)
    }

    override fun onReNoteButtonClicked(targetId: String?, note: Note?) {
        context.startActivity(EditNoteActivity.getIntent(context, targetId, NoteType.RE_NOTE))
    }

    override fun onDetailButtonClicked(note: Note) {

        val dialog = DetailDialog.getInstance(connectionProperty, note)
        onShowReactionDialog(dialog)


    }
    override fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>) {
        context.startActivity(ImageViewerActivity.getIntent(context, clickedImageUrlCollection, clickedIndex))
    }

    override fun onMediaPlayClicked(fileProperty: FileProperty) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fileProperty.url)))
    }

    private fun reactionSelected(viewData: NoteViewData, reactionType: String?){
        Log.d("NoteClickListener", "targetId :${viewData.toShowNote.id}, reactionType: $reactionType")
        when {
            viewData.toShowNote.myReaction != null -> deleteReaction(viewData.toShowNote.id)
            reactionType != null -> this.sendReaction(reactionType = reactionType, viewData = viewData)
            else -> showReactionSelectorView(viewData)
        }
    }

    private fun showReactionSelectorView(viewData: NoteViewData) {
        Log.d("NoteClickListener", "showReactionSelectorViewが呼び出された")
        val reactionDialog = ReactionDialog.getInstance(viewData.toShowNote.id, object : ReactionDialog.CallBackListener{
            override fun callBack(noteId: String?, reactionParameter: String) {
                if(noteId != null){
                    Log.d("TimelineFragment", "成功した")
                    sendReaction(reactionType = reactionParameter, viewData = viewData)
                }
            }
        })
        onShowReactionDialog(reactionDialog)

    }

    private fun deleteReaction(noteId: String){
        GlobalScope.launch{
            reactionRepository.deleteReaction(noteId)
        }
    }

    private fun sendReaction(viewData: NoteViewData, reactionType: String) {
        GlobalScope.launch{
            val result = reactionRepository.sendReaction(viewData.toShowNote.id, reactionType)
            if(result){
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

    }


}