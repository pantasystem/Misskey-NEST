package org.panta.misskeynest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.android.synthetic.main.activity_test.*
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.MetaProperty
import org.panta.misskeynest.network.OkHttpConnection
import org.panta.misskeynest.adapter.RecyclerAdapter
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
