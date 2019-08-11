package org.panta.misskeynest.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
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
            reactionDialog.arguments = args
            reactionDialog.mCallBackListener = callBackListener


            return reactionDialog
        }
    }

    private var mCallBackListener: CallBackListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val targetNoteId = arguments?.getString(TARGET_NOTE_ID)


        val listener = object : ItemClickListener<String> {
            override fun onClick(item: String) {
                val iconId = ReactionConstData.getAllConstReactionList().firstOrNull {
                    it == item
                }
                val reaction = if( iconId != null ){
                    item
                }else{
                    ":$item:"
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
        val layoutManager = LinearLayoutManager(context)
        content.reaction_viewer.layoutManager = layoutManager
        content.cancel_button.setOnClickListener {
            dismiss()
        }

        content.emoji_search_box.addTextChangedListener(
            object : TextWatcher{
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if( content.emoji_search_box.text?.isNotBlank() == true ){
                        val search = list.indexOfFirst {
                            it.name.contains(content.emoji_search_box.text)
                        }
                        Log.d("ReactionDialog", "探索した位置 $search,　内容 ${content.emoji_search_box.text}")
                        layoutManager.scrollToPosition(search)

                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
            }
        )



        return builder.create()
    }
}