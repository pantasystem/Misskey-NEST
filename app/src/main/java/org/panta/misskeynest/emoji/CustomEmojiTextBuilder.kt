package org.panta.misskeynest.emoji

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import org.panta.misskeynest.entity.EmojiProperty
import org.panta.misskeynest.util.*
import java.io.File

class CustomEmojiTextBuilder(private val context: Context, private val size: Int) {

    companion object{
        private val bitmapCache = BitmapCache()

        private val spannableCache = SpannableStringBuilderCache()
    }

    private val svgParser = SVGParser()
    private val emojiFileList = context.fileList().map{
        File(context.filesDir, it)
    }
    private var emojiMap = emojiFileList.map{
        it.name.replace(":", "").split(".")[0] to it
    }.toMap()


    private var mSpannable = SpannableStringBuilder()
    private var mCharBuilder = StringBuilder()

    private fun updateEmojiMap(){
        synchronized(emojiMap){
            val emojiFileList = context.fileList().map{
                File(context.filesDir, it)
            }
            emojiMap = emojiFileList.map{
                it.name.replace(":", "").split(".")[0] to it
            }.toMap()
        }

    }

    @Synchronized suspend fun createSpannableString(text: String, emojiList: List<EmojiProperty>?): SpannableString?{
        mSpannable = SpannableStringBuilder()
        val cache = spannableCache.get(text)
        if(cache != null){
            return SpannableString(cache)
        }


        try{
            //val spannable = SpannableStringBuilder()

            val charArray = text.toCharArray()
            val iterator = charArray.iterator()

            //var charTmp = StringBuilder()
            mCharBuilder = StringBuilder()
            while(iterator.hasNext()){
                val c = iterator.next()

                if(c == ':'){

                    insertEmoji(emojiList, c)

                }else{
                    mCharBuilder.append(c)
                }
            }

            //最後に残ったテキストを解析
            val last = mCharBuilder.toString()
            if(last.contains(":")){
                insertEmoji(emojiList, null)
            }else{
                mSpannable.append(last)
            }

            spannableCache.put(text, mSpannable)
            return SpannableString(mSpannable)

        }catch(e: Exception){
            Log.d("CustomEmoji", "error", e)
            return null
        }
    }

    private suspend fun insertEmoji(emojiList: List<EmojiProperty>?, c: Char?){
        val midwayText = mCharBuilder.toString()
        val emojiFile = getEmojisFile(midwayText)
        val notesEmoji = emojiList?.firstOrNull{ it.name == midwayText }

        Log.d("midwayText", "中間テキストは: $midwayText")
        if(emojiFile == null && notesEmoji == null){
            //絵文字ではない場合
            appendNormalText(midwayText, c)
        }else{
            val bitmap = when {
                emojiFile != null -> getBitmapFromEmojiName(midwayText, size)
                notesEmoji != null -> getBitmapFromEmojiProperty(notesEmoji, size)
                else -> null
            }

            if(bitmap == null){
                appendNormalText(midwayText, c)
            }else{
                appendImageSpanFromBitmap(mSpannable, midwayText, bitmap)

            }
        }


        mCharBuilder = StringBuilder()
    }

    private fun appendNormalText(text: String, c: Char?){
        mSpannable
            .append(text)
        if(c != null){
            mSpannable.append(c)
        }
    }

    private fun getEmojisFile(emoji: String): File?{
        return emojiMap[emoji.replace(":", "")]
    }



    private fun appendImageSpanFromBitmap(spannable: SpannableStringBuilder, text: String, bitmap: Bitmap){
        try{
            val imageSpan = ImageSpan(context, bitmap)
            val start = spannable.length
            spannable.append(text)
            spannable.setSpan(imageSpan, start - 1, start + text.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }catch(e: Exception){
            Log.d("CustomEmoji", "appendImageSpan method. エラー発生")

        }
    }

    private suspend fun getBitmapFromEmojiName(name: String, size: Int): Bitmap?{
        val cacheBitmap = getBitmapFromCache(name)
        if(cacheBitmap != null) return cacheBitmap

        //ファイル内にないかを調べる
        val fileBitmap = getBitmapFromFile(name, size)
        if(fileBitmap != null) return fileBitmap

        return null
    }

    private suspend fun getBitmapFromEmojiProperty(property: EmojiProperty, size: Int): Bitmap?{

        val cacheBitmap = getBitmapFromCache(property.name)
        if(cacheBitmap != null) return cacheBitmap

        //ファイル内にないかを調べる
        val fileBitmap = getBitmapFromFile(property.name, size)
        if(fileBitmap != null) return fileBitmap

        //それでもない場合
        val meta = property.getExtension()
        val bitmap = if(meta == "svg"){
            val textSvg = property.saveSVG(context.openFileOutput(property.createFileName(), Context.MODE_PRIVATE))
            svgParser.getBitmapFromString(textSvg, size, size)
        }else{
            resizeBitmap(property.saveImage(context.openFileOutput(property.createFileName(), Context.MODE_PRIVATE)), size)
        }
        updateEmojiMap()
        synchronized(bitmapCache){
            bitmapCache.put(property.name, bitmap)
        }
        return bitmap
    }

    private fun getBitmapFromCache(name: String): Bitmap?{
        //LRUキャッシュに画像がないかを調べる
        return bitmapCache.get(name)
    }

    private fun getBitmapFromFile(name: String, size: Int): Bitmap?{
        val bitmapFile = emojiMap[name]
        return if(bitmapFile?.exists() == true){
            val bitmap = if(bitmapFile.path.endsWith(".svg")){
                svgParser.getBitmapFromFile(bitmapFile, size, size)
            }else{
                resizeBitmap(BitmapFactory.decodeFile(bitmapFile.path), size)
            }
            //キャッシュに保存
            synchronized(bitmapCache){
                bitmapCache.put(name, bitmap)
            }
            bitmap
        }else{
            null
        }
    }
    private fun resizeBitmap(bitmap: Bitmap, size: Int): Bitmap {
        val scale = size / bitmap.width.toDouble()
        return Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
    }

}