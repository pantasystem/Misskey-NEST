package org.panta.misskey_nest.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.widget.TextView
import java.io.File

class CustomEmoji(private val context: Context, private val emojiFileList: List<File>){
    private val emojiMap = emojiFileList.map{
        it.name.replace(":", "").split(".")[0] to it
    }.toMap()

    fun setTextView(textView: TextView, text: String, size: Int? = 60){
        val spannable = SpannableStringBuilder()

        val splitTextList = text.split(":")
        for(t in splitTextList){
            val emojiFile = emojiMap[t]
            if(emojiFile == null){
                spannable.append(t)
            }else{
                val bitmap = BitmapFactory.decodeFile(emojiFile.path)
                val tmpSize = if(bitmap.width < 50){
                    size ?: 80
                }else{
                    size
                }

                val showBitmap = if(tmpSize == null){
                    bitmap
                }else{
                    val scale = tmpSize / bitmap.width.toDouble()
                    Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
                }


                val imageSpan = ImageSpan(context, showBitmap)
                val start = spannable.length
                spannable.append(t)
                spannable.setSpan(imageSpan, start, start + t.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        textView.text = spannable
    }
}