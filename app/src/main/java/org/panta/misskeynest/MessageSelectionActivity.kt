package org.panta.misskeynest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_message_selection.*
import org.panta.misskeynest.fragment.MessageSelectionFragment
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator

class MessageSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_selection)

        setSupportActionBar(message_selection_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()
        if( connectionProperty == null ){
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }
        val fragment = MessageSelectionFragment.getInstance(connectionProperty)

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.message_selection_base, fragment)
        ft.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when( item?.itemId ){
            android.R.id.home ->{
                finish()
            }
        }
        return true
    }
}
