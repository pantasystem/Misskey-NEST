package org.panta.misskeynest.view_presenter

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_follow_follower.*
import kotlinx.android.synthetic.main.activity_notification.*
import org.panta.misskeynest.R
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.view_presenter.notification.NotificationFragment
import org.panta.misskeynest.view_presenter.user_auth.AuthActivity

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(TwitterEmojiProvider())

        setContentView(R.layout.activity_notification)


        setSupportActionBar(notification_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val connectionInfo = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()
        if(connectionInfo == null){
            startActivity(Intent(this, AuthActivity::class.java))
        }else{
            val sp = supportFragmentManager
            val st = sp.beginTransaction()
            st.replace(R.id.notification_background, NotificationFragment.getInstance(connectionInfo))
            st.commit()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)


    }

}
