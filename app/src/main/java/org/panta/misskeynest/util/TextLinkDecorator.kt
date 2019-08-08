package org.panta.misskeynest.util

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import java.util.regex.Pattern

class TextLinkDecorator {


    fun execute(textView: TextView){

        //FIXME 上手くリンク化できない
        val pattern = Pattern.compile("@([A-Za-z0-9_-]+)")
        val scheme = "https://misskey.io"
        val mentionFilter = Linkify.TransformFilter { match, url ->
            Log.d("TextLinkDecorator", "リンクを踏んだ $url")
            return@TransformFilter "://user"
        }
        Linkify.addLinks(textView, pattern, "misskeynest", null, mentionFilter)


        val webUrlFilter = Linkify.TransformFilter { match, url ->
            Log.d("TextLinkDecorator", "Webリンクを踏んだ $url")
            return@TransformFilter url
        }
        //Linkify.addLinks(textView, Linkify.WEB_URLS)
        Linkify.addLinks(textView, Patterns.WEB_URL, null, null, webUrlFilter)
        textView.linksClickable = true
        textView.movementMethod = LinkMovementMethod.getInstance()

    }
}