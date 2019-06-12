package org.panta.misskeynest.util

import android.graphics.drawable.BitmapDrawable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.util.LruCache

class SpannableStringBuilderCache : LruCache<String, SpannableStringBuilder>(16) {

    override fun sizeOf(key: String?, value: SpannableStringBuilder?): Int {

        if(value == null){
            return 0
        }

        var totalSize: Int = 0
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
        return totalSize
    }


}