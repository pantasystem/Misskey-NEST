package org.panta.misskeynest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_message.*
import org.panta.misskeynest.fragment.MessageFragment
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.viewdata.MessageViewData

class MessageActivity : AppCompatActivity() {

    companion object{
        private const val MESSAGE_VIEW_DATA_PROPERTY = "MessageActivity.MessageViewDataProperty"
        fun getIntent(context: Context, viewData: MessageViewData): Intent{
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(MESSAGE_VIEW_DATA_PROPERTY, viewData)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        setSupportActionBar(activity_message_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val message = intent.getSerializableExtra(MESSAGE_VIEW_DATA_PROPERTY) as MessageViewData
        val connectionProperty = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()

        title = message.roomTitle

        if( connectionProperty == null){
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }
        val ft = supportFragmentManager.beginTransaction()
        val fragment = MessageFragment.getInstance(connectionProperty, message)
        ft.add(R.id.activity_message_base, fragment)
        ft.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
