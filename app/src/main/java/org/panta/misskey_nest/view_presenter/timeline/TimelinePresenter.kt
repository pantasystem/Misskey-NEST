package org.panta.misskey_nest.view_presenter.timeline

import android.util.Log
import org.panta.misskey_nest.dialog.ReactionDialog
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.entity.Note
import org.panta.misskey_nest.interfaces.ErrorCallBackListener
import org.panta.misskey_nest.interfaces.IBindScrollPosition
import org.panta.misskey_nest.interfaces.IBindStreamingAPI
import org.panta.misskey_nest.interfaces.IItemRepository
import org.panta.misskey_nest.repository.*
import org.panta.misskey_nest.usecase.NoteUpdater
import org.panta.misskey_nest.usecase.ObservationNote
import org.panta.misskey_nest.usecase.PagingController
import org.panta.misskey_nest.view_data.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View,
                        override var bindScrollPosition: IBindScrollPosition, private val mTimeline: IItemRepository<NoteViewData>, info: ConnectionProperty)
    : TimelineContract.Presenter, ErrorCallBackListener, IBindStreamingAPI{


    private val pagingController =
        PagingController<NoteViewData>(mTimeline, this)


    private val mReaction = Reaction(domain = info.domain, authKey = info.i)

    private val observationStreaming = ObservationNote(this, bindScrollPosition, info)


    override fun getNewTimeline() {
        pagingController.getNewItems {
            mView.showNewTimeline(it)
        }
    }

    override fun getOldTimeline() {
        pagingController.getOldItems {
            mView.showOldTimeline(it)
        }
    }

    override fun initTimeline() {
        pagingController.getInit {
            mView.showInitTimeline(it)
        }
    }

    override fun callBack(e: Exception) {
        mView.stopRefreshing()
    }

    override fun sendReaction(noteId: String, viewData: NoteViewData, reactionType: String) {
        mReaction.sendReaction(noteId, reactionType){
            if(it){
                Log.d("TimelinePresenter", "sendReaction成功したようだ")
                //val updatedNote = NoteUpdater().addReaction(reactionType, viewData, hasMyReaction = true)
                //mView.showUpdatedNote(updatedNote)
            }else{
                mView.onError("リアクションの送信に失敗した")
                Log.d("TimelinePresenter", "sendReaction失敗しちゃった・・")
            }
        }
        //mView.showUpdatedNote(viewData)


    }

    override fun setReactionSelectedState(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?) {
        if(targetId != null && note != null){
            when {
                viewData.toShowNote.myReaction != null -> deleteReaction(viewData.toShowNote.id)
                reactionType != null -> this.sendReaction(noteId = note.id, reactionType = reactionType, viewData = viewData)
                else -> mView.showReactionSelectorView(targetId, viewData)
            }

        }
    }

    override fun captureNote(noteId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun addFirstNote(data: NoteViewData) {

    }

    override fun onRemoveNote(data: NoteViewData) {

    }

    override fun onUpdateNote(data: NoteViewData) {
        mView.showUpdatedNote(data)
    }

    private fun deleteReaction(noteId: String){
        mReaction.deleteReaction(noteId)
    }

    override fun onRefresh() {
        observationStreaming.onRefresh()
    }


}