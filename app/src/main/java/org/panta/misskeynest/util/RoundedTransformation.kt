package org.panta.misskeynest.util

import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth

//https://gist.github.com/aprock/6213395
class RoundedTransformation// radius is corner radii in dp
// margin is the board in dp
    (
    private val radius: Int, private val margin: Int  // dp
) : com.squareup.picasso.Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(
            RectF(
                margin.toFloat(),
                margin.toFloat(),
                (source.width - margin).toFloat(),
                (source.height - margin).toFloat()
            ), radius.toFloat(), radius.toFloat(), paint
        )

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "rounded(radius=" + Integer.toString(radius) + ", margin=" + Integer.toString(margin) + ")"
    }
}