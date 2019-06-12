package org.panta.misskeynest.interfaces

import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.view_data.NoteViewData
import java.util.*

interface INoteClickListener {
    fun onNoteClicked(targetId: String?, note: Note?)
    fun onReplyButtonClicked(targetId: String?, note: Note?)
    fun onReNoteButtonClicked(targetId: String?, note: Note?)
    fun onReactionClicked(targetId: String?, note: Note?, viewData: NoteViewData,reactionType: String?)
    fun onDescriptionButtonClicked(targetId: String?, note: Note?)
    fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>)
    fun onMediaPlayClicked(fileProperty: FileProperty)

}