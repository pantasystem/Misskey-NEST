package org.panta.misskey_nest.util

import android.view.View
import android.widget.TextView
import org.panta.misskey_nest.emoji.CustomEmoji

class InjectionText(private val customEmoji: CustomEmoji){
    fun injectionTextGoneWhenNull(text: String?, view: TextView){
        if(text == null){
            view.visibility = View.GONE
            return
        }

        injection(text, view)
    }

    fun injectionTextInvisible(text: String?, view:TextView, targetValue: String?){
        if(text == targetValue){
            view.visibility = View.INVISIBLE
            return
        }

        injection(text, view)
    }

    fun injection(text: String?, view: TextView){
        if(text?.contains(":") == true){
            customEmoji.setTextView(view, text)
        }else{
            view.text= text

        }
        view.visibility = View.VISIBLE

    }

}