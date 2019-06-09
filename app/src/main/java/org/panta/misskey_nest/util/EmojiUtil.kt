package org.panta.misskey_nest.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.panta.misskey_nest.entity.EmojiProperty
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun EmojiProperty.getExtension(): String?{
    val meta = this.type
    return when {
        meta == null -> this.url?.split(".")?.lastOrNull()
            ?:this.uri?.split(".")?.lastOrNull()
        meta.contains("svg") -> "svg"
        else -> "png"
    }
}

fun EmojiProperty.createFileName(): String{
    val name = this.name
    return "$name.${getExtension()}"
}

suspend fun EmojiProperty.saveImage(outputStream: FileOutputStream){
    Log.d("EmojiUtil", "saveImageを呼び出しました: $url")
    val connection = URL(this.url).openConnection() as HttpsURLConnection
    connection.connect()
    val inputStream  = BufferedInputStream(connection.inputStream)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

}

suspend fun EmojiProperty.saveSVG(outputStream: FileOutputStream){
    Log.d("EmojiUtil", "saveSVGを呼び出しました: $url")
    val connection = URL(this.url).openConnection() as HttpsURLConnection
    connection.connect()
    val inputStream = BufferedInputStream(connection.inputStream)


    var next = inputStream.read()
    while(next != -1){
        outputStream.write(next)
        next = inputStream.read()
    }
    outputStream.flush()
    outputStream.close()

}

suspend fun EmojiProperty.save(context: Context){
    val meta = this.getExtension()
    val ops = context.openFileOutput(this.createFileName(), Context.MODE_PRIVATE)

    Log.d("EmojiUtil", "fileName ${this.createFileName()}")
    if(meta == "svg"){
        this.saveSVG(ops)
    }else{
        this.saveImage(ops)
    }
}

