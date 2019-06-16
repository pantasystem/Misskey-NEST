package org.panta.misskeynest.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_reaction_viewer.view.*
import org.panta.misskeynest.R
import org.panta.misskeynest.adapter.ReactionViewerAdapter
import org.panta.misskeynest.constant.ReactionConstData
import org.panta.misskeynest.interfaces.ItemClickListener
import java.io.File
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
            //args.putSerializable(REACTION_DIALOG_CALL_BACK_ARGS_CODE, callBackListener)
            reactionDialog.arguments = args
            reactionDialog.mCallBackListener = callBackListener

            //Log.d("TimelineFragment", "Is fm null? ${fm?:"Yes Null"}")
            //FIXME ミックスタイムラインから呼び出されると落ちる
            //reactionDialog.setTargetFragment(fragmentManager, reactionRequestCode)
            //reactionDialog.show(activity?.supportFragmentManager, "reaction_tag")
            return reactionDialog
        }
    }

    private var mCallBackListener: CallBackListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        //val dialog = Dialog(context!!)
        //dialog.setContentView(R.layout.dialog_reaction_viewer)


        val targetNoteId = arguments?.getString(TARGET_NOTE_ID)

        //val callBackListener = arguments?.getSerializable(REACTION_DIALOG_CALL_BACK_ARGS_CODE) as CallBackListener?

        val listener = object : ItemClickListener<String> {
            override fun onClick(e: String) {
                val iconId = ReactionConstData.getAllConstReactionList().firstOrNull {
                    it == e
                }
                val reaction = if( iconId != null ){
                    e
                }else{
                    ":$e:"
                }
                Log.d("ReactionDialog", "選択したりアクションは $reaction")
                mCallBackListener?.callBack(targetNoteId, reaction)
                dismiss()
            }
        }

        Log.d("ReactionDialog", "targetNoteIdは${targetNoteId.toString()}")


        val list = activity?.fileList()?.map{
            File(activity?.filesDir, it)
        }!!
        Log.d("ReactionDialog", "list $list")

        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(R.layout.dialog_reaction_viewer, null)
        builder.setView(content)
        val adapter = ReactionViewerAdapter(list, listener)
        content.reaction_viewer.adapter = adapter
        content.reaction_viewer.layoutManager = LinearLayoutManager(context)
        content.cancel_button.setOnClickListener {
            dismiss()
        }



        return builder.create()
    }
}