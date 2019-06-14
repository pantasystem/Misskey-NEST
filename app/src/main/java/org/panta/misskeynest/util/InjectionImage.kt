package org.panta.misskeynest.util

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.panta.misskeynest.R

class InjectionImage{
    fun injectionImage(imageUrl: String, imageView: ImageView, isSensitive: Boolean?){
        imageView.visibility = View.VISIBLE

        if(isSensitive != null && isSensitive){
            imageView.setImageResource(R.drawable.sensitive_image)
        }else{
            Picasso
                .get()
                .load(imageUrl)
                .into(imageView)
        }

    }

    fun roundInjectionImage(imageUrl: String, imageView: ImageView, radius:Int = 30){
        imageView.visibility = View.VISIBLE
        val trfm = RoundedTransformation(radius, 0)
        Picasso
            .get()
            .load(imageUrl)
            .transform(trfm)
            .into(imageView)
    }
}