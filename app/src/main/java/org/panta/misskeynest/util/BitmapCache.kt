package org.panta.misskeynest.util

import android.graphics.Bitmap
import android.util.LruCache

class BitmapCache private constructor(): LruCache<String, Bitmap>(30 * 1024 * 1024){
    //private val cache = ArrayDeque<Pair<String, Bitmap>>()
    companion object{
        private val bitmapCache = BitmapCache()
        fun getInstance(): BitmapCache{
            return bitmapCache
        }
    }

    override fun sizeOf(key: String?, value: Bitmap?): Int {
        return if(value?.allocationByteCount != null) value.allocationByteCount else 0
    }

    override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Bitmap?, newValue: Bitmap?) {
        if(oldValue?.isRecycled == true){
            oldValue.recycle()
        }
    }

}