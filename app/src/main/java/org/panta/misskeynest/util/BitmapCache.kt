package org.panta.misskeynest.util

import android.graphics.Bitmap
import android.util.LruCache

class BitmapCache: LruCache<String, Bitmap>(30){
    //private val cache = ArrayDeque<Pair<String, Bitmap>>()

    override fun sizeOf(key: String?, value: Bitmap?): Int {
        return if(value?.allocationByteCount != null) value.allocationByteCount else 0
    }

    override fun entryRemoved(evicted: Boolean, key: String?, oldValue: Bitmap?, newValue: Bitmap?) {
        if(oldValue?.isRecycled == true){
            oldValue.recycle()
        }
    }

}