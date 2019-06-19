package org.panta.misskeynest.view.timeline

import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.IItemRepository
import org.panta.misskeynest.repository.Reaction
import org.panta.misskeynest.usecase.PagingController
import org.panta.misskeynest.viewdata.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View,
                       mTimeline: IItemRepository<NoteViewData>, info: ConnectionProperty)
    : TimelineContract.Presenter, ErrorCallBackListener{


    private val pagingController =
        PagingController<NoteViewData>(mTimeline, this)


    private val mReaction = Reaction(domain = info.domain, authKey = info.i)

    //private val observationStreaming = ObservationNote(mAdapterOperator, bindScrollPosition, info)


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
        pagingController.init {
            mView.showInitTimeline(it)
        }
    }

    override fun callBack(e: Exception) {
        mView.stopRefreshing()
    }

    /*override fun sendReaction(noteId: String, viewData: NoteViewData, reactionType: String) {
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


    }*/

    /*override fun setReactionSelectedState(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?) {
        if(targetId != null && note != null){
            when {
                viewData.toShowNote.myReaction != null -> deleteReaction(viewData.toShowNote.id)
                reactionType != null -> this.sendReaction(noteId = note.id, reactionType = reactionType, viewData = viewData)
                else -> mView.showReactionSelectorView(targetId, viewData)
            }

        }
    }*/

    override fun captureNote(noteId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /*override fun addFirstNote(data: NoteViewData) {

    }

    override fun onRemoveNote(data: NoteViewData) {

    }

    override fun onUpdateNote(data: NoteViewData) {
        mView.showUpdatedNote(data)
    }*/

    private fun deleteReaction(noteId: String){
        mReaction.deleteReaction(noteId)
    }

    override fun onRefresh() {
        //observationStreaming.onRefresh()
    }


}