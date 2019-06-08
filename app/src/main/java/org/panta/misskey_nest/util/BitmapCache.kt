package org.panta.misskey_nest.util

import android.graphics.Bitmap
import android.util.Log
import java.util.*

class BitmapCache(private val maxCount: Int){
    private val cache = ArrayDeque<Pair<String, Bitmap>>()
    private var bitmapByte: Long = 0

    fun pushCache(key: String, bitmap: Bitmap){

        val cacheBitmap = getBitmap(key)
        if(cacheBitmap != null){
            return
        }

        if(cache.count() < maxCount){
            Log.d("CacheBitmap", "Bitmapをキャッシュしました　key:$key")

            cache.add(key to bitmap)
            synchronized(bitmapByte){
                bitmapByte += bitmap.byteCount
            }
        }else{
            val pair = cache.removeFirst()
            val tmpBitmap = pair.second
            synchronized(bitmapByte){
                bitmapByte -= tmpBitmap.byteCount
            }
        }

    }

    fun getBitmap(key: String): Bitmap?{
        synchronized(cache){
            val iterator = cache.iterator()
            while(iterator.hasNext()){
                val element = iterator.next()
                if(element.first == key){
                    Log.d("CacheBitmap", "getBitmapから取得　key:$key")
                    return element.second
                }
            }
            return null
        }
    }


}