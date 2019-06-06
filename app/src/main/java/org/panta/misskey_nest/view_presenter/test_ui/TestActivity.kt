package org.panta.misskey_nest.view_presenter.test_ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.item_single_image.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_nest.R
import org.panta.misskey_nest.entity.ConnectionProperty
import org.panta.misskey_nest.entity.MetaProperty
import org.panta.misskey_nest.network.OkHttpConnection
import org.panta.misskey_nest.repository.PersonalRepository
import org.panta.misskey_nest.storage.SharedPreferenceOperator
import java.io.File
import java.net.URL

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val lm = LinearLayoutManager(this)

        test_recycler_view.layoutManager = lm
        val filesList = fileList().map{
            File(filesDir, it)
        }
        test_recycler_view.adapter = RecyclerAdapter(filesList, this)


        /*val info = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()

        GlobalScope.launch{
            val meta = getMeta(info!!)

        }*/
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
