package org.panta.misskeynest.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.R
import org.panta.misskeynest.entity.MetaProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.util.createFileName
import org.panta.misskeynest.util.saveImage
import org.panta.misskeynest.util.saveSVG
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class EmojiDownloadService : Service() {

    override fun onBind(intent: Intent): IBinder? {
                return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val  notification = NotificationCompat.Builder(this).apply{
            setContentTitle("カスタム絵文字を更新中")
            setContentTitle("カスタム絵文字を更新しています。")
            setSmallIcon(R.drawable.misskey_icon)
        }.build()
        startForeground(3, notification)
        Log.d("EmojiDownloadService", "onStartCommand　サービスを開始しました！！！！")

        Log.d("EmojiDownloadService", "onCreateが呼び出された")
        val info = PersonalRepository(SharedPreferenceOperator(this)).getDomain()
        if(info == null){
            Log.d("EmojiDownloadService" ,"ドメイン情報がNULLのため終了する")
            stopSelf()
        }else{
            saveEmoji(info)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()


    }

    private fun saveEmoji(domain: String){
        GlobalScope.launch{

            try{
                val meta = getMeta(domain)
                meta?.emojis?: return@launch
                meta.emojis.forEach{
                    val fileList = applicationContext.fileList()
                    if(fileList.any{ file -> file.contains(it.name)} ){
                        Log.d(this.toString(), "既に存在しています: ${it.name}")
                    }else{

                        //val fileName = it.name.split("/").last()
                        if(it.type?.endsWith("svg+xml") == true){
                            //saveSvg(it.url!!, it.name)
                            it.saveSVG(applicationContext.openFileOutput(it.createFileName() , Context.MODE_PRIVATE))
                        }else{
                            //saveImage(it.url!!, it.name)
                            it.saveImage(applicationContext.openFileOutput(it.createFileName() , Context.MODE_PRIVATE))
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

                val fos = applicationContext.openFileOutput("$fileName.png", Context.MODE_PRIVATE)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

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
                val inputStream = BufferedInputStream(connection.inputStream)


                val ops = applicationContext.openFileOutput("$fileName.svg", Context.MODE_PRIVATE)
                var next = inputStream.read()
                while(next != -1){
                    ops.write(next)
                    next = inputStream.read()
                }
                ops.flush()
                ops.close()

                Log.d(this.toString(), "SVG保存成功 $fileName")

            }catch(e: Exception){
                Log.d(this.toString(), "保存中にエラー $url, $fileName", e)
            }
        }
    }

    private suspend fun getMeta(domain: String): MetaProperty?{
        return try{
            val net = OkHttpConnection().postString(URL("$domain/api/meta"), "")
            jacksonObjectMapper().readValue(net!!)
        }catch(e: Exception){
            Log.d(this.toString(), "meta取得中にエラー発生", e)
            null
        }
    }
}
