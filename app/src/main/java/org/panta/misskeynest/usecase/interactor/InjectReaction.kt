package org.panta.misskeynest.usecase.interactor

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.util.BitmapCache
import org.panta.misskeynest.util.SVGParser
import java.io.File

class InjectReaction(private val imageView: ImageView, private val textView: TextView) {


    fun setTextReaction(emoji: String){
        textView.visibility = View.VISIBLE
        imageView.visibility = View.GONE
        textView.text = emoji
    }

    fun setReactionFromResource(id: Int){
        textView.visibility = View.GONE
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(id)
    }

    fun setImageFromFile(file: File){
        if(file.name.endsWith(".svg")){
            imageView.visibility = View.INVISIBLE
            textView.visibility = View.GONE
            GlobalScope.launch {
                try{

                    val bitmap = getBitmapFromSvgFile(file)

                    Handler(Looper.getMainLooper()).post{
                        try{
                            imageView.setImageBitmap(bitmap)
                            imageView.visibility = View.VISIBLE
                        }catch(e: Exception){
                            Log.d("ReactionHolder", "error", e)
                        }
                    }
                }catch(e: Exception){
                    Log.d("ReactionHolder", "error", e)
                }

            }



            //Log.d("ReactionHolder", "SVGタイプの画像が来た")
        }else if( ! file.name.endsWith(".svg")){
            textView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            Picasso
                .get()
                .load(file)
                .fit()
                .into(imageView)
        }
    }

    private fun getBitmapFromSvgFile(file: File): Bitmap {
        val cache = BitmapCache.getInstance()

        val name = file.name.split(".")[0]

        val cacheBitmap: Bitmap? = cache.get(name)

        return if( cacheBitmap == null ){
            val tmpBitmap = SVGParser().getBitmapFromFile(file, 50, 50)
            cache.put(file.name.split(".")[0], tmpBitmap)
            tmpBitmap
        }else{
            cacheBitmap
        }
    }
}