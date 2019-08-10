package org.panta.misskeynest.presenter

import android.util.Log
import org.panta.misskeynest.contract.TimelineContract
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.filter.NoteFilter
import org.panta.misskeynest.interfaces.ErrorCallBackListener
import org.panta.misskeynest.interfaces.IItemFilter
import org.panta.misskeynest.repository.IItemRepository
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.remote.ReactionRepository
import org.panta.misskeynest.streaming.INoteCaptureUseCase
import org.panta.misskeynest.usecase.interactor.PagingController
import org.panta.misskeynest.viewdata.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View,
                        private val mNoteCaptureUseCase: INoteCaptureUseCase,
                        mTimeline: IItemRepository<Note>,
                        info: ConnectionProperty,
                        private val settingRepository: PersonalRepository
): TimelineContract.Presenter, ErrorCallBackListener{


    val filter: IItemFilter<Note, NoteViewData> = NoteFilter()
    private val pagingController =
        PagingController<Note, NoteViewData>(mTimeline, this, filter)


    private val mReaction = ReactionRepository(domain = info.domain, authKey = info.i)

    //private val key = "${mView}_${type?.name}"

    override fun start() {

        if( settingRepository.isBackgroundUpdatable ){
            mNoteCaptureUseCase.start()
        }
        mView.showLoading()
        initTimeline()
    }

    //一時停止
    override fun pause() {
        if( settingRepository.isBackgroundUpdatable ){

        }else{
            Log.d("TMPresenter", "バックグラウンド更新不可")
            mNoteCaptureUseCase.pause()
        }
    }

    override fun resume() {

        if( mNoteCaptureUseCase.isActive() ){

        }else{
            mNoteCaptureUseCase.start()
        }
    }

    override fun destroy() {
        Log.d("TMPresenter", "destroyが呼び出された")
        mNoteCaptureUseCase.clear()
    }

    override fun getNewTimeline() {
        pagingController.getNewItems {
            mView.showNewTimeline(it)
            mNoteCaptureUseCase.addAll(it)
            if(mNoteCaptureUseCase.isActive()){

            }else{
                mNoteCaptureUseCase.start()
            }
            mView.showTimeline()
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
            mView.showTimeline()
        }
    }

    override fun initTimeline() {
        pagingController.init {
            mView.showInitTimeline(it)
            mNoteCaptureUseCase.clear()
            mNoteCaptureUseCase.addAll(it)
            mView.showTimeline()
        }
    }

    override fun callBack(e: Exception) {
        mView.stopRefreshing()
    }

}