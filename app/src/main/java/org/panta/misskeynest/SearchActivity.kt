package org.panta.misskeynest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_search.*
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.fragment.TimelineFragment
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()!!

        val searchWord = intent.data?.getQueryParameter("searchWord")
        if(searchWord != null){
            search_keyword_box.setText(searchWord)
            createFragment(connectionProperty, searchWord)
        }

        search_button.setOnClickListener{
            //result_area
            createFragment(connectionProperty, search_keyword_box.text.toString())

        }
    }

    private fun createFragment(connectionProperty: ConnectionProperty, word: String){
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        val fragment = TimelineFragment.getInstance(connectionProperty, word, false)
        ft.replace(R.id.result_area, fragment)
        ft.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
