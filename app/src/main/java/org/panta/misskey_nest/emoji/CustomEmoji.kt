package org.panta.misskey_nest.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.TextView
import com.caverock.androidsvg.SVG
import org.panta.misskey_nest.util.BitmapCache
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class CustomEmoji(private val context: Context, private val emojiFileList: List<File>){

    companion object{

        //private val bitmapCache = BitmapCache(10)

        fun getBitmapFromSVG(file: File, width: Int, height: Int): Bitmap{
            if( ! file.path.endsWith(".svg") ) throw IllegalAccessException("This file is not svg file. You must use svg file")

            //val cache = bitmapCache.getBitmap(file.path)
            /*if(cache != null){
                return cache
            }*/

            val stream = BufferedReader(InputStreamReader(file.inputStream()))
            val builder = StringBuilder()
            while(true){
                val next: String? = stream.readLine()
                if(next == null){
                    break
                }else{
                    builder.append(next)
                }
            }

            val svg = SVG.getFromString(builder.toString())

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            svg.renderToCanvas(canvas)
            //bitmapCache.pushCache(file.path, bitmap)

            return bitmap

        }
    }

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
                val bitmap = if(emojiFile.path.endsWith(".svg")){
                    getBitmapFromSVG(emojiFile, textView.textSize.toInt(), textView.textSize.toInt())
                }else{
                    resizeBitmap(BitmapFactory.decodeFile(emojiFile.path), textView.textSize.toInt())
                }



                val imageSpan = ImageSpan(context, bitmap)
                val start = spannable.length
                spannable.append(t)
                spannable.setSpan(imageSpan, start, start + t.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        //Log.d("CustomEmoji", "描画しました")
        textView.text = spannable
    }



    private fun resizeBitmap(bitmap: Bitmap, size: Int?): Bitmap{
        val tmpSize = if(bitmap.width < 50){
            size ?: 80
        }else{
            size
        }

        return if(tmpSize == null){
            bitmap
        }else{
            val scale = tmpSize / bitmap.width.toDouble()
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
        }
    }
}