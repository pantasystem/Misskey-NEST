package org.panta.misskey_nest.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskey_nest.R
import org.panta.misskey_nest.entity.Note


@Suppress("UNREACHABLE_CODE")
class DescriptionDialog : DialogFragment(){

    companion object{
        const val NOTE = "DescriptionDialog_NOTE"
    }


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

        val note = arguments?.getSerializable(NOTE)


        val dialogView = activity!!.layoutInflater.inflate(android.R.layout.simple_list_item_multiple_choice, null, false)

        val item = arrayOf<CharSequence>("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ（開発者向け）")
        val dialog = AlertDialog.Builder(activity).apply{

            setTitle("詳細")
            setSingleChoiceItems(item, 0){ dialog, which ->
                when(which){
                    4 ->{
                        AlertDialog.Builder(activity).apply{
                            if(note is Note){
                                setMessage(note.toString())
                            }
                            setPositiveButton(android.R.string.ok){i ,b->
                                dismiss()
                            }
                        }.show()
                    }
                }
                dismiss()
            }

            setItems(item){ dialog, which->


            }
        }
        dialog.setView(dialogView)
        return dialog.create()

    }
}