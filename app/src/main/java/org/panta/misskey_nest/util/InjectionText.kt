package org.panta.misskey_nest.util

import android.view.View
import android.widget.TextView
import org.panta.misskey_nest.emoji.CustomEmoji
import org.panta.misskey_nest.entity.EmojiProperty

class InjectionText(private val customEmoji: CustomEmoji){
    fun injectionTextGoneWhenNull(text: String?, view: TextView, emojis: List<EmojiProperty>? = null){
        if(text == null){
            view.visibility = View.GONE
            return
        }

        injection(text, view, emojis)
    }

    fun injectionTextInvisible(text: String?, view:TextView, targetValue: String?, emojis: List<EmojiProperty>? = null){
        if(text == targetValue){
            view.visibility = View.INVISIBLE
            return
        }

        injection(text, view, emojis)
    }

    fun injection(text: String?, view: TextView, emojis: List<EmojiProperty>? = null){
        if(text?.contains(":") == true){
            customEmoji.setTextView(view, text, emojis)
        }else{
            view.text= text

        }
        view.visibility = View.VISIBLE

    }

}