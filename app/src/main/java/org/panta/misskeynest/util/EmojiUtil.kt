package org.panta.misskeynest.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import org.panta.misskeynest.entity.EmojiProperty
import java.io.*
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

suspend fun EmojiProperty.saveImage(outputStream: FileOutputStream): Bitmap{
    Log.d("EmojiUtil", "saveImageを呼び出しました: $url")
    val connection = URL(this.url).openConnection() as HttpsURLConnection
    connection.connect()
    val inputStream  = BufferedInputStream(connection.inputStream)
    val bitmap = BitmapFactory.decodeStream(inputStream)

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

    return bitmap
}

suspend fun EmojiProperty.saveSVG(outputStream: FileOutputStream): String{
    Log.d("EmojiUtil", "saveSVGを呼び出しました: $url")
    val connection = URL(this.url).openConnection() as HttpsURLConnection
    connection.connect()
    //val inputStream = BufferedInputStream(connection.inputStream)
    val writer = BufferedWriter(OutputStreamWriter(outputStream))
    val reader = BufferedReader(InputStreamReader(connection.inputStream))


    val builder = StringBuilder()
    var next = reader.readLine()
    while(next != null){
        writer.write(next)
        builder.append(next)
        next = reader.readLine()
    }

    writer.flush()
    writer.close()
    reader.close()
    return builder.toString()
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

