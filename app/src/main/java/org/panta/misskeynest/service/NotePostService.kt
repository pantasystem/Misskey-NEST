package org.panta.misskeynest.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.entity.FileProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.view.user_auth.AuthActivity
import java.io.File
import java.net.URL

class NotePostService : Service() {

    companion object{
        const val FILE_NAME_ARRAY_CODE = "NotePostServiceFileNameArrayCode"
        const val NOTE_BUILDER_CODE = "NotePostServiceNoteBuilderPropertyCode"
    }

    private lateinit var i: String
    private lateinit var domain: String

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val sharedPref = PersonalRepository(SharedPreferenceOperator(this))
        val info = sharedPref.getConnectionInfo()
        if(info == null){
            startActivity(Intent(applicationContext, AuthActivity::class.java))
            stopSelf()
            return
        }else{
            i = info.i
            domain = info.domain
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("NotePostService", "onStartCommand")

        val noteBuilder = intent?.getSerializableExtra(NOTE_BUILDER_CODE) as CreateNoteProperty.Builder

        val fileNameArray = intent.getStringArrayExtra(FILE_NAME_ARRAY_CODE)
        GlobalScope.launch{
            try{
                /*val file = fileNameArray?.map { File(it) }?.map{
                    jacksonObjectMapper().readValue<FileProperty>(uploadFile(it)!!).id!!
                }*/
                val files = fileNameArray?.map{
                    File(it)
                }

                val fileIdList = ArrayList<String>()
                files?.forEach{
                    val response = uploadFile(it)
                    if(response == null){
                        Log.d("NotePostService", "fileのアップロードに失敗")
                    }else{
                        try{
                            Log.d("NotePostService", "fileのアップロードに成功")
                            val value = jacksonObjectMapper().readValue<FileProperty>(response)

                            fileIdList.add(value.id!!)
                        }catch(e: Exception){
                            Log.d("NotePostService", "fileのアップロードに失敗", e)
                        }
                    }
                }

                noteBuilder.fileIds = if(fileIdList.isEmpty()){
                    null
                }else{
                    fileIdList
                }

                val noteProperty = noteBuilder.create()
                val result = OkHttpConnection().postString(URL("$domain/api/notes/create"), jacksonObjectMapper().writeValueAsString(noteProperty))
                if(result == null){
                    Log.d("NotePostService", "投稿に失敗")
                }else{
                    Log.d("NotePostService", "投稿に成功")

                }
                stopSelf()
            }catch(e: Exception){
                Log.w("NotePostService", "error", e)
            }


        }

        return START_NOT_STICKY

    }

    private suspend fun uploadFile(file: File): String?{
        val connection = OkHttpConnection()
        return connection.postFile(URL("$domain/api/drive/files/create"), i = i, file = file, force = true)

    }
}
