package org.panta.misskeynest.view_presenter.timeline

import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView
import org.panta.misskeynest.view_data.NoteViewData

interface TimelineContract{

    interface View : BaseView<Presenter>{
        fun showNewTimeline(list: List<NoteViewData>)
        fun showOldTimeline(list: List<NoteViewData>)
        fun showInitTimeline(list: List<NoteViewData>)
        fun stopRefreshing()
        fun onError(errorMsg: String)
        fun showUpdatedNote(noteViewData: NoteViewData)
        fun showReactionSelectorView(targetId: String, viewData: NoteViewData)
    }

    interface Presenter : BasePresenter {
        fun getNewTimeline()
        fun getOldTimeline()
        fun initTimeline()
        fun captureNote(noteId: String)
        fun sendReaction(noteId: String, viewData: NoteViewData, reactionType: String)
        fun setReactionSelectedState(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?)
        fun onRefresh()
    }
}