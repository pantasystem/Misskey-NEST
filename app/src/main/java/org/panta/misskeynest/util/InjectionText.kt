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

class InjectionText(var isEnableLink: Boolean = false){



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
            view.visibility = View.VISIBLE

            TextLinkDecorator().execute(view)

            return
        }
        if( ! text.contains(":")){
            //setAndVisibleView()
            view.text = text
            view.visibility = View.VISIBLE

            TextLinkDecorator().execute(view)

            return
        }


        GlobalScope.launch{

            try{
                val builder = CustomEmojiTextBuilder(view.context, (view.textSize.toInt() * 1.2).toInt())

                val span = builder.createSpannableString(text, emojis)

                Handler(Looper.getMainLooper()).post {
                    view.visibility = View.VISIBLE
                    if( span == null ){
                        view.text = text
                    }else{
                        view.text = span
                    }
                    //Log.d("InjectionText", "描画した内容 ${view.text}")
                    if(isEnableLink){
                        TextLinkDecorator().execute(view)
                    }

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