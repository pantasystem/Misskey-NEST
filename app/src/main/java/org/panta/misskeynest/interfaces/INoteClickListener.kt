package org.panta.misskeynest.interfaces

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.viewdata.NoteViewData

interface INoteClickListener {
    fun onNoteClicked(note: Note)
    fun onReplyButtonClicked(targetId: String?, note: Note?)
    fun onReNoteButtonClicked(targetId: String?, note: Note?)
    fun onReactionClicked(targetId: String?, note: Note?, viewData: NoteViewData,reactionType: String?)
    fun onDetailButtonClicked(viewData: NoteViewData)
    fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>)
    fun onMediaPlayClicked(fileProperty: FileProperty)

}