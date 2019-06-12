package org.panta.misskeynest.util

import android.text.SpannableString
import android.util.LruCache

class SpannableStringCache : LruCache<String, SpannableString>(20 ) {

    override fun sizeOf(key: String?, value: SpannableString?): Int {

        if(value == null){
            return 0
        }

        /*var totalSize: Int = 0
        val imageSpans = value.getSpans(0, value.length, ImageSpan::class.java)
        imageSpans.forEach{
            val drawable = it.drawable
            if(drawable is BitmapDrawable){
                totalSize += drawable.bitmap.allocationByteCount * drawable.bitmap.height
                //Log.d("SpannableCache", "in sizeOf method.　BitmapDrawableにキャスト成功")
            }else{
                totalSize += it.source?.length ?: 0
            }
        }
        Log.d("SpannableCache", "トータルサイズは: $totalSize")
        return totalSize*/
        return value.length
    }


}