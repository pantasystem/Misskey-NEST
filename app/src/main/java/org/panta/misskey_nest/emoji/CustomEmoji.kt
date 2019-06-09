package org.panta.misskey_nest.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.caverock.androidsvg.SVG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.util.BitmapCache
import org.panta.misskey_nest.util.SpannableStringBuilderCache
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class CustomEmoji(private val context: Context){

    companion object{

        private val bitmapCache = BitmapCache(10)

        fun getBitmapFromSVG(file: File, width: Int, height: Int): Bitmap{
            if( ! file.path.endsWith(".svg") ) throw IllegalAccessException("This file is not svg file. You must use svg file")

            val cache = bitmapCache.getBitmap(file.path)
            if(cache != null){
                return cache
            }

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
            bitmapCache.pushCache(file.path, bitmap)

            return bitmap

        }
    }
    private val emojiFileList = context.fileList().map{
        File(context.filesDir, it)
    }
    private val emojiMap = emojiFileList.map{
        it.name.replace(":", "").split(".")[0] to it
    }.toMap()

    private val spannableCache = SpannableStringBuilderCache()

    fun setTextView(textView: TextView, text: String){
        val cache = spannableCache.get(text)
        if(cache != null){
            textView.text = cache
            textView.visibility = View.VISIBLE
            return
        }
        Handler(Looper.getMainLooper()).post{
            textView.visibility = View.INVISIBLE
        }
        GlobalScope.launch {
            try{
                val spannable = SpannableStringBuilder()

                val charArray = text.toCharArray()
                val iterator = charArray.iterator()

                var charTmp = StringBuilder()
                while(iterator.hasNext()){
                    val c = iterator.next()

                    if(c == ':'){
                        val midwayText = charTmp.toString()
                        Log.d("CustomEmoji", "midwayText: $midwayText")
                        val emojiFile = getEmojisFile(midwayText)
                        if(emojiFile != null){

                            appendImageSpan(spannable, midwayText, emojiFile, textView.textSize.toInt())

                        }else{
                            //通常の文字列だった場合
                            spannable.append(midwayText)
                            spannable.append(c)

                        }

                        charTmp = StringBuilder()


                    }else{
                        charTmp.append(c)
                    }
                }

                //最後に残ったテキストを解析
                val last = charTmp.toString()
                val emojiFile = emojiMap[last.replace(":", "")]
                if(emojiFile != null){
                    appendImageSpan(spannable, last, emojiFile, textView.textSize.toInt())
                }else{
                    spannable.append(last)
                }

                spannableCache.put(text, spannable)
                Handler(Looper.getMainLooper()).post{
                    textView.text = spannable
                    textView.visibility = View.VISIBLE
                }

            }catch(e: Exception){
                Log.d("CustomEmoji", "error", e)
            }

        }

    }

    fun getEmojisBitmap(emojiFile: File, size: Int): Bitmap{

        return if(emojiFile.path.endsWith(".svg")){
            getBitmapFromSVG(emojiFile, size, size)
        }else{
            resizeBitmap(BitmapFactory.decodeFile(emojiFile.path), size)
        }
    }

    fun getEmojisFile(emoji: String): File?{
        return emojiMap[emoji.replace(":", "")]
    }

    private fun appendImageSpan(spannable: SpannableStringBuilder, text: String, emojiFile: File, size: Int){
        try{
            val finalSize = (size.toDouble() * 1.2).toInt()
            val bitmap = getEmojisBitmap(emojiFile, finalSize)
            val imageSpan = ImageSpan(context, bitmap)
            val start = spannable.length
            spannable.append(text)
            spannable.setSpan(imageSpan, start - 1, start + text.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }catch(e: Exception){
            Log.d("CustomEmoji", "appendImageSpan method. エラー発生 text:$text, emojiFile: ${emojiFile.path}")
        }

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