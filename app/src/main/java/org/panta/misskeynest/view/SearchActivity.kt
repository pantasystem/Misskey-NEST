package org.panta.misskeynest.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search.*
import org.panta.misskeynest.R
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.repository.SearchRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.view.timeline.TimelineFragment

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()!!
        search_button.setOnClickListener{
            //result_area
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            val fragment = TimelineFragment.getInstance(connectionProperty)
            fragment.mNoteRepository = SearchRepository(connectionProperty, search_keyword_box.text.toString())
            ft.replace(R.id.result_area, fragment)
            ft.commit()
        }
    }
}
