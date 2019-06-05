package org.panta.misskey_nest.util

import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.panta.misskey_nest.interfaces.IImageViewInjection

class ImageViewInjection : IImageViewInjection{
    override fun setImageFromOnline(url: String?, imageView: ImageView) {
        Picasso
            .get()
            .load(url)
            .into(imageView)
    }
}