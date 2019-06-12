package org.panta.misskeynest.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.emoji.CustomEmoji
import org.panta.misskeynest.emoji.CustomEmojiTextBuilder
import org.panta.misskeynest.entity.EmojiProperty

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
            val builder = CustomEmojiTextBuilder(view.context, view.textSize.toInt())
            Handler(Looper.getMainLooper()).post{
                view.visibility = View.INVISIBLE
            }
            GlobalScope.launch{
                try{
                    val span = builder.createSpannableString(text, emojis)

                    Handler(Looper.getMainLooper()).post{
                        view.text = span
                        view.visibility = View.VISIBLE
                    }
                }catch(e: Exception){
                    Log.e("InjectionText", "error $text", e)
                }


            }
        }else{
            view.text= text

        }
        view.visibility = View.VISIBLE

    }

}