package org.panta.misskeynest.util

import android.graphics.Bitmap
import android.graphics.Canvas
import com.caverock.androidsvg.SVG
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class SVGParser{
    fun getBitmapFromFile(file: File, width: Int, height: Int): Bitmap {
        if( ! file.path.endsWith(".svg") ) throw IllegalAccessException("This file is not svg file. You must use svg file")

        return getBitmapFromInputStream(file.inputStream(), width, height)
    }

    fun getBitmapFromInputStream(inputStream: InputStream, width: Int, height: Int): Bitmap{
        val stream = BufferedReader(InputStreamReader(inputStream))
        val builder = StringBuilder()
        while(true){
            val next: String? = stream.readLine()
            if(next == null){
                break
            }else{
                builder.append(next)
            }
        }

        return getBitmapFromString(builder.toString(), width, height)
    }

    fun getBitmapFromString(svg: String, width: Int, height: Int): Bitmap{
        val vectorSvg = SVG.getFromString(svg)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorSvg.renderToCanvas(canvas)

        return bitmap
    }

}