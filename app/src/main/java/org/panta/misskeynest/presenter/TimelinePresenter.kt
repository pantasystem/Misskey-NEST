package org.panta.misskeynest.presenter

import org.panta.misskeynest.contract.TimelineContract
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.filter.NoteFilter
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.repository.remote.ReactionRepository
import org.panta.misskeynest.usecase.INoteCaptureUseCase
import org.panta.misskeynest.viewdata.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View, private val mNoteCaptureUseCase: INoteCaptureUseCase,
                        mTimeline: IItemRepository<Note>, info: ConnectionProperty)
    : TimelineContract.Presenter, ErrorCallBackListener{


    val filter: IItemFilter<Note, NoteViewData> = NoteFilter()
    private val pagingController =
        PagingController<Note, NoteViewData>(mTimeline, this, filter)


    private val mReaction = ReactionRepository(domain = info.domain, authKey = info.i)



    override fun getNewTimeline() {
        pagingController.getNewItems {
            mView.showNewTimeline(it)
            mNoteCaptureUseCase.addAll(it)
            if(mNoteCaptureUseCase.isActive()){

            }else{
                mNoteCaptureUseCase.start()
            }
        }
    }

    override fun getOldTimeline() {
        pagingController.getOldItems {
            mView.showOldTimeline(it)
            mNoteCaptureUseCase.addAll(it)
            if(mNoteCaptureUseCase.isActive()){

            }else{
                mNoteCaptureUseCase.start()
            }
        }
    }

    override fun initTimeline() {
        pagingController.init {
            mView.showInitTimeline(it)
            mNoteCaptureUseCase.clear()
            mNoteCaptureUseCase.addAll(it)
        }
    }

    override fun callBack(e: Exception) {
        mView.stopRefreshing()
    }



    override fun captureNote(noteId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




    private fun deleteReaction(noteId: String){
        mReaction.deleteReaction(noteId)
    }

    override fun onRefresh() {
        //observationStreaming.onRefresh()
    }


}