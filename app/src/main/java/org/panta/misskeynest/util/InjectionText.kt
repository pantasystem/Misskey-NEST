package org.panta.misskeynest.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.emoji.CustomEmojiTextBuilder
import org.panta.misskeynest.entity.EmojiProperty

class InjectionText{
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

    fun injection(text: String?, view: TextView, emojis: List<EmojiProperty>?){

        if(text == null){
            return
        }

        if(emojis == null || emojis.isEmpty()){
            //setAndVisibleView()
            view.text = text
            return
        }
        if( ! text.contains(":")){
            //setAndVisibleView()
            view.text = text
            return
        }


        GlobalScope.launch{
            val count = countEmoji(text, emojis)

            val tmpTextBuilder = StringBuilder()
            (0 until count).forEach{ _ ->
                tmpTextBuilder.append("　")
            }

            Handler(Looper.getMainLooper()).post{
                Log.d("InjectionText", "擬似テキスト表示")
                view.text = tmpTextBuilder.toString()
                view.visibility = View.VISIBLE
            }

            try{
                val builder = CustomEmojiTextBuilder(view.context, view.textSize.toInt())

                val span = builder.createSpannableString(text, emojis)

                Handler(Looper.getMainLooper()).post {
                    view.text = span
                    view.visibility = View.VISIBLE
                }
            }catch (e: Exception){
                Log.e("InjectionText", "error $text", e)

            }
        }


    }

    private fun countEmoji(text: String, emojis: List<EmojiProperty>): Int{
        val splitText = text.split(':')
        return (splitText.count{k ->
            emojis.any{l ->
                l.name == k
            }
        }.toDouble() * 0.9).toInt()

    }

}