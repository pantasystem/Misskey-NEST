package org.panta.misskeynest.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.Note
import org.panta.misskeynest.repository.remote.NoteRepository
import org.panta.misskeynest.util.copyToClipboad


class DetailDialog : DialogFragment(){

    companion object{
        private const val NOTE = "DescriptionDialog_NOTE"
        private const val CONNECTION_PROPERTY_KEY = "DetailDialogConnectionPropertyKey"

        fun getInstance(connectionProperty: ConnectionProperty, note: Note): DetailDialog{
            return DetailDialog().apply{
                val bundle = Bundle()
                bundle.putSerializable(NOTE, note)
                bundle.putSerializable(CONNECTION_PROPERTY_KEY, connectionProperty)
                this.arguments = bundle
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val note = arguments?.getSerializable(NOTE) as Note
        val connectionProperty = arguments?.getSerializable(CONNECTION_PROPERTY_KEY) as ConnectionProperty

        val itemList = arrayListOf("内容をコピー", "リンクをコピー", "お気に入り", "ウォッチ", "デバッグ")
        if(note.user?.id == connectionProperty.userPrimaryId){
            itemList.add("削除")
        }

        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = inflater.inflate(android.R.layout.select_dialog_item, null)
        builder.setView(content)
        builder.setTitle("詳細")
            .setItems(itemList.toTypedArray()){_, which ->
                when(which){
                    0 -> copyToClipboad(context, note.text.toString())
                    1 -> copyToClipboad(context, "${connectionProperty.domain}/notes/${note.id}")
                    2,3 -> Toast.makeText(context, "未実装ですごめんなさい", Toast.LENGTH_SHORT)
                    4 ->{
                        AlertDialog.Builder(activity).apply{
                            val noteString = note.toString().replace(",","\n")
                            setMessage(noteString)
                            setPositiveButton(android.R.string.ok){i ,b->
                            }
                        }.show()
                    }
                    5 ->{
                        GlobalScope.launch {
                            val b = NoteRepository(connectionProperty).remove(note)
                            Handler(Looper.getMainLooper()).post{
                                val statusMsg = if(b) "成功しました" else "失敗しました"
                                Toast.makeText(context, statusMsg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            .setNegativeButton((android.R.string.cancel)){ _ ,_->
                dismiss()
            }


        return builder.create()


    }
}