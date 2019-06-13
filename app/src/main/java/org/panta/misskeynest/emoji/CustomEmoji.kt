package org.panta.misskeynest.emoji

import android.content.Context

@Deprecated("CustomEmojiTextBuilderを使用すること") class CustomEmoji(private val context: Context){

    /*companion object{
        private val bitmapCache = BitmapCache()

        private val spannableCache = SpannableStringCache()
    }

    private val svgParser = SVGParser()
    private val emojiFileList = context.fileList().map{
        File(context.filesDir, it)
    }
    private var emojiMap = emojiFileList.map{
        it.name.replace(":", "").split(".")[0] to it
    }.toMap()


    fun updateEmojiMap(){
        val emojiFileList = context.fileList().map{
            File(context.filesDir, it)
        }
        emojiMap = emojiFileList.map{
            it.name.replace(":", "").split(".")[0] to it
        }.toMap()
    }

    fun setTextView(textView: TextView, text: String, notesEmojiList: List<EmojiProperty>? = null){
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
                        //val emojiFile = getEmojisFile(midwayText)
                        val emojiFile: File? = null
                        val notesEmoji = notesEmojiList?.firstOrNull{ it.name == midwayText }

                        Log.d("midwayText", "中間テキストは: $midwayText")
                        if(emojiFile == null && notesEmoji == null){
                            //絵文字ではない場合
                            spannable
                                .append(midwayText)
                                .append(c)
                        }else{
                            val size = textView.textSize.toInt()
                            val bitmap = when {
                                emojiFile != null -> getBitmapFromEmojiName(midwayText, size)
                                notesEmoji != null -> getBitmapFromEmojiProperty(notesEmoji, size)
                                else -> null
                            }

                            if(bitmap == null){
                                spannable
                                    .append(midwayText)
                                    .append(c)
                            }else{
                                appendImageSpanFromBitmap(spannable, midwayText, bitmap)

                            }
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
                    //appendImageSpanFromFile(spannable, last, emojiFile, textView.textSize.toInt())
                    //TODO 最後に残ったテキストを追加するようにする
                }else{
                    spannable.append(last)
                }

                //spannableCache.put(text, spannable)
                Handler(Looper.getMainLooper()).post{
                    textView.text = spannable
                    textView.visibility = View.VISIBLE
                }

            }catch(e: Exception){
                Log.d("CustomEmoji", "error", e)
            }

        }

    }

    /*fun getEmojisFile(emoji: String): File?{
        return emojiMap[emoji.replace(":", "")]
    }*/



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

    suspend fun getBitmapFromEmojiName(name: String, size: Int): Bitmap?{
        val cacheBitmap = getBitmapFromCache(name)
        if(cacheBitmap != null) return cacheBitmap

        //ファイル内にないかを調べる
        val fileBitmap = getBitmapFromFile(name, size)
        if(fileBitmap != null) return fileBitmap

        return null
    }

    suspend fun getBitmapFromEmojiProperty(property: EmojiProperty, size: Int): Bitmap?{

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
        bitmapCache.put(property.name, bitmap)
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
            bitmapCache.put(name, bitmap)
            bitmap
        }else{
            null
        }
    }
    private fun resizeBitmap(bitmap: Bitmap, size: Int): Bitmap{
        val scale = size / bitmap.width.toDouble()
        return Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
    }
*/


}