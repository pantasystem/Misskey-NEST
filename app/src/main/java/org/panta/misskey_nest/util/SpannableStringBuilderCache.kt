package org.panta.misskey_nest.util

import android.text.SpannableStringBuilder

class SpannableStringBuilderCache {

    companion object{
        private val spannableMap = HashMap<String, SpannableStringBuilder>()
    }

    fun get(text: String): SpannableStringBuilder?{
        synchronized(spannableMap){
            return spannableMap[text]
        }
    }

    fun put(text: String, spannable: SpannableStringBuilder){
        synchronized(spannableMap){
            spannableMap[text] = spannable
        }
    }
}