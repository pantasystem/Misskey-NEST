package org.panta.misskeynest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.fragment.TimelineFragment

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()!!
        search_button.setOnClickListener{
            //result_area
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            val fragment = TimelineFragment.getInstance(connectionProperty, search_keyword_box.text.toString(), false)
            ft.replace(R.id.result_area, fragment)
            ft.commit()
        }
    }
}
