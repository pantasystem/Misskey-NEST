package org.panta.misskeynest.util

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.panta.misskeynest.R
import java.io.File

class InjectionImage{
    fun injectionImage(imageUrl: String, imageView: ImageView, isSensitive: Boolean?){
        imageView.visibility = View.VISIBLE

        if(isSensitive != null && isSensitive){
            setSensitiveImage(imageView)
        }else{
            Picasso
                .get()
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(imageView)
        }

    }

    fun injectionImage(file: File, imageView: ImageView , isSensitive: Boolean?){
        imageView.visibility = View.VISIBLE

        if( isSensitive != null && isSensitive ){
            setSensitiveImage(imageView)
        }else{
            Picasso
                .get()
                .load(file)
                .centerCrop()
                .fit()
                .into(imageView)
        }
    }

    fun injectionImage(id: Int, imageView: ImageView, isSensitive: Boolean?){
        imageView.visibility = View.VISIBLE

        if( isSensitive != null && isSensitive ){
            setSensitiveImage(imageView)
        }else{
            Picasso
                .get()
                .load(id)
                .centerCrop()
                .fit()
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


    private fun setSensitiveImage(imageView: ImageView){
        Picasso
            .get()
            .load(R.drawable.sensitive_image)
            .centerCrop()
            .fit()
            .into(imageView)
    }
}