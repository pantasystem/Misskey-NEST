package org.panta.misskeynest.util

import android.text.SpannableStringBuilder
import java.util.*
import kotlin.collections.HashMap

class SpannableStringBuilderCache {

    companion object{
        private val spannableMap = HashMap<String, SpannableStringBuilder>()
        private val mapLogDeque = ArrayDeque<String>()
    }

    fun get(text: String): SpannableStringBuilder?{
        synchronized(spannableMap){
            return spannableMap[text]
        }
    }

    fun put(text: String, spannable: SpannableStringBuilder){
        synchronized(spannableMap){
            val cache = spannableMap[text]
            if(cache == null){
                spannableMap[text] = spannable
                synchronized(mapLogDeque){
                    mapLogDeque.add(text)
                }
            }else{
                return
            }

        }

        synchronized(spannableMap){
            if(mapLogDeque.size > 20){
                val key = mapLogDeque.removeFirst()
                spannableMap.remove(key)
            }
        }


    }
}