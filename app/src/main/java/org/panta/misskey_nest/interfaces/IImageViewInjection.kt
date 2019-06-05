package org.panta.misskey_nest.interfaces

import android.widget.ImageView
import java.net.URL

interface IImageViewInjection {
    fun setImageFromOnline(url: String?, imageView: ImageView)
}