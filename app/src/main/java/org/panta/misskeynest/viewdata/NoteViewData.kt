package org.panta.misskeynest.viewdata

import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.entity.ReactionCountPair
import org.panta.misskeynest.interfaces.ID
import org.panta.misskeynest.usecase.interactor.NoteFormatUseCase
import java.io.Serializable
import java.util.*

//isReply = 返信であるか, isOriginReplay = 返信先であるか(背景を暗くする)
//isOriginReplyは途中で追加されたデータなので最新のデータにはならない
//isReplyが最新となるがViewにはisOriginReplayが先に挿入される
//通常のNote,RNの場合は両方ともがFalseとなる
data class NoteViewData(override val id: String,
                        override val isIgnore: Boolean, val note: Note, val toShowNote: Note, val type: NoteFormatUseCase.NoteType, val reactionCountPairList: List<ReactionCountPair>, var updatedAt: Date):Serializable, ID