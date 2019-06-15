package org.panta.misskeynest.view.note_editor


import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.interfaces.BasePresenter
import org.panta.misskeynest.interfaces.BaseView
import java.io.File

//そこまで複雑ではないのでリッチアクティビティで行く方針
interface EditNoteContract{
    interface View : BaseView<Presenter> {
        fun onError(msg: String)
        fun showFileManager()
        fun showCloudFileManager()
        fun startPost(builder: CreateNoteProperty.Builder, files: Array<String>)
        fun showImagePreview()
        fun addImagePreviewItem(file: File)
    }

    interface Presenter : BasePresenter {

        fun setText(text: String)
        fun postNote()
        fun setNoteType(type: Int, targetId: String?)
        fun setVisibility(visibility: String)
        fun setFile(file: File)
        fun removeFile(index: Int)

    }
}