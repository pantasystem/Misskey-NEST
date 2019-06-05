package org.panta.misskey_nest.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.GridView
import org.panta.misskey_nest.R
import org.panta.misskey_nest.adapter.ReactionDialogGridViewAdapter
import org.panta.misskey_nest.constant.ReactionConstData
import org.panta.misskey_nest.util.convertDp2Px
import java.io.Serializable

class ReactionDialog : DialogFragment(){

    interface CallBackListener : Serializable {
        fun callBack(noteId: String?, reactionParameter: String)
    }

    companion object{
        const val SELECTED_REACTION_CODE = "REACTION_DIALOG_SELECTED_REACTION_CODE"
        const val TARGET_NOTE_ID = "REACTION_DIALOG_TARGET_NOTE_ID"
        private const val REACTION_DIALOG_CALL_BACK_ARGS_CODE = "REACTION_DIALOG_CALL_BACK_ARGS_CODE"

        fun getInstance(targetId: String, callBackListener: CallBackListener): ReactionDialog{
            val reactionDialog = ReactionDialog()
            val args = Bundle()
            args.putString(ReactionDialog.TARGET_NOTE_ID, targetId)
            args.putSerializable(REACTION_DIALOG_CALL_BACK_ARGS_CODE, callBackListener)
            reactionDialog.arguments = args

            //Log.d("TimelineFragment", "Is fm null? ${fm?:"Yes Null"}")
            //FIXME ミックスタイムラインから呼び出されると落ちる
            //reactionDialog.setTargetFragment(fragmentManager, reactionRequestCode)
            //reactionDialog.show(activity?.supportFragmentManager, "reaction_tag")
            return reactionDialog
        }
    }

    private val callBackListener: ReactionDialog.CallBackListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val targetNoteId = arguments?.getString(TARGET_NOTE_ID)

        val callBackListener = arguments?.getSerializable(REACTION_DIALOG_CALL_BACK_ARGS_CODE) as CallBackListener?

        Log.d("ReactionDialog", "targetNoteIdは${targetNoteId.toString()}")

        val gridView = GridView(activity)
        gridView.apply {
            columnWidth = convertDp2Px(50F, context).toInt()
            numColumns = GridView.AUTO_FIT

        }
        gridView.adapter =
            ReactionDialogGridViewAdapter(context!!, R.layout.item_reaction_dialogs_icon, ReactionConstData.getAllConstReactionList())
        gridView.setOnItemClickListener { _, _, i, _ ->
            //val intent = Intent()
            //intent.putExtra(SELECTED_REACTION_CODE, ReactionConstData.getAllConstReactionList()[i])
            //intent.putExtra(TARGET_NOTE_ID, targetNoteId)
            //targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            callBackListener?.callBack(targetNoteId, ReactionConstData.getAllConstReactionList()[i])
            dismiss()
        }

        val builder = AlertDialog.Builder(activity).apply {
            setView(gridView)
            setNegativeButton("やめる") { _, _ ->

            }

        }
        return builder.create()
    }
}