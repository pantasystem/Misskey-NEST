package org.panta.misskeynest.view.test_ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_single_image.view.*
import org.panta.misskeynest.R
import java.io.File

class RecyclerAdapter(val list: List<File>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View, private val context: Context): RecyclerView.ViewHolder(itemView){
        private val textView: TextView = itemView.test_text_view
        private val imageView: ImageView = itemView.image_view
        fun onBind(file: File){
            //textView.text = file
            Picasso
                .get()
                .load(file)
                .into(imageView)
            /*val bitmap = BitmapFactory.decodeStream(context.openFileInput(file))
            imageView.setImageBitmap(bitmap)*/
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_single_image, p0, false)
        return ViewHolder(inflater, context)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.onBind(list[p1])
    }
}