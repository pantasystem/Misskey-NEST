package org.panta.misskey_nest.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


fun copyToClipboad(context: Context?, text: String, label: String = ""){
    val cm = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    cm?.primaryClip = ClipData.newPlainText(label, text)
}