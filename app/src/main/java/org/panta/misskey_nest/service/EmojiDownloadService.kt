package org.panta.misskey_nest.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.entity.MetaProperty
import org.panta.misskey_nest.network.HttpsConnection
import org.panta.misskey_nest.network.OkHttpConnection
import org.panta.misskey_nest.repository.PersonalRepository
import org.panta.misskey_nest.storage.SharedPreferenceOperator
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class EmojiDownloadService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val info = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()
        if(info == null){
            return
        }else{
            saveEmoji(info)
        }
    }

    private fun saveEmoji(info: ConnectionProperty){
        GlobalScope.launch{

            try{
                val meta = getMeta(info)
                meta?.emojis?: return@launch
                meta.emojis.forEach{
                    val fileList = applicationContext.fileList()
                    if(fileList.any{ file -> file.contains(it.name)} ){
                        Log.d(this.toString(), "既に存在しています: ${it.name}")
                    }else{

                        //val fileName = it.name.split("/").last()
                        if(it.type.endsWith("svg+xml")){
                            //saveSvg(it.url!!, "${it.name}.svg")
                        }else{
                            saveImage(it.url!!, "${it.name}.png")
                        }
                    }
                }
            }catch (e: Exception){
                Log.d(this.toString(), "error", e)
            }

        }
    }

    private fun saveImage(url: String, fileName: String){
        GlobalScope.launch{
            try{
                val connection = URL(url).openConnection() as HttpsURLConnection
                connection.connect()
                val inputStream  = BufferedInputStream(connection.inputStream)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val fos = applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                /*var len = inputStream.read()
                while(len != -1){
                    fos.write(len)
                    len = inputStream.read()
                }
                fos.flush()*/
                Log.d(this.toString(), "保存成功 $fileName")

            }catch(e: Exception){
                Log.d(this.toString(), "保存中にエラー $url, $fileName", e)
            }
        }
    }

    private fun saveSvg(url: String, fileName: String){
        GlobalScope.launch{
            try{

                val connection = URL(url).openConnection() as HttpsURLConnection
                connection.connect()
                //val inputStream =
            }catch(e: Exception){
                Log.d(this.toString(), "保存中にエラー $url, $fileName", e)
            }
        }
    }

    private suspend fun getMeta(info: ConnectionProperty): MetaProperty?{
        val map = mapOf("detail" to false, "i" to info.i)
        return try{
            val net = OkHttpConnection().postString(URL("${info.domain}/api/meta"), jacksonObjectMapper().writeValueAsString(map))
            jacksonObjectMapper().readValue(net!!)
        }catch(e: Exception){
            Log.d(this.toString(), "meta取得中にエラー発生", e)
            null
        }
    }
}
