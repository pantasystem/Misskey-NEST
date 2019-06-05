package org.panta.misskey_nest.view_presenter.timeline

import org.panta.misskey_nest.entity.Note
import org.panta.misskey_nest.interfaces.BasePresenter
import org.panta.misskey_nest.interfaces.BaseView
import org.panta.misskey_nest.view_data.NoteViewData

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