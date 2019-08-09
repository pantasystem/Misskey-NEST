package org.panta.misskeynest.util

import android.net.Uri
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import java.util.regex.Pattern

class TextLinkDecorator {

    //var currentWord: String? = null

    fun execute(textView: TextView){

        //pattern1 @?([A-Za-z0-9_-]+)(?:\.[a-zA-Z0-9-]+)*?
        //pattern2
        val pattern = Pattern.compile("@([A-Za-z0-9_-]+)@?([A-Za-z0-9_-]+)*\\.?([a-zA-Z0-9_-]+)*")

        val mentionFilter = Linkify.TransformFilter { _, url ->
            Log.d("TextLinkDecorator", "リンクを踏んだ $url")
            val builder = Uri.Builder()
            builder.authority("user")
            builder.appendQueryParameter("userId", url)
            val uri = ":${builder.build()}"
            return@TransformFilter uri
        }
        Linkify.addLinks(textView, pattern, "misskeynest", null, mentionFilter)

        val hashTagPattern = Pattern.compile("#([A-Za-z0-9_-ぁ-んァ-ヶ一-龥_]+)")
        val hashTagFilter = Linkify.TransformFilter { match, url ->
            val builder = Uri.Builder()
            builder.authority("search")
                .appendQueryParameter("searchWord", url)
            return@TransformFilter ":${builder.build()}"
        }
        Linkify.addLinks(textView, hashTagPattern, "misskeynest",null, hashTagFilter)


        val webUrlFilter = Linkify.TransformFilter { match, url ->
            Log.d("TextLinkDecorator", "Webリンクを踏んだ $url")
            return@TransformFilter url
        }
        Linkify.addLinks(textView, Patterns.WEB_URL, null, null, webUrlFilter)
        textView.linksClickable = true
        textView.movementMethod = LinkMovementMethod.getInstance()

    }
}