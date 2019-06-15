package org.panta.misskeynest.view.follow_follower

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_follow_follower.*
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.view.user_auth.AuthActivity

class FollowFollowerActivity : AppCompatActivity() {

    companion object{
        const val USER_ID_TAG = "FollowFollowerActivityUserIdTag"
        const val FOLLOW_FOLLOWER_TYPE = "FollowFollowerActivityFollowingFollowerType"
        //const val CONNECTION_INFO = "FollowFollowerActivityConnectionInfo"

        fun startActivity(context: Context, type: FollowFollowerType, userId: String){
            val intent = Intent(context, FollowFollowerActivity::class.java)
            //intent.putExtra(FollowFollowerActivity.CONNECTION_INFO, info)
            intent.putExtra(FollowFollowerActivity.FOLLOW_FOLLOWER_TYPE, type.ordinal)
            intent.putExtra(FollowFollowerActivity.USER_ID_TAG, userId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(TwitterEmojiProvider())

        setContentView(R.layout.activity_follow_follower)
        setSupportActionBar(follow_follower_tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userId = intent.getStringExtra(USER_ID_TAG)!!
        val tmpType = intent.getIntExtra(FOLLOW_FOLLOWER_TYPE, FollowFollowerType.FOLLOWING.ordinal)
        val connectionInfo = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()
        if(connectionInfo == null){
            startActivity(Intent(applicationContext, AuthActivity::class.java))
            finish()
            return
        }
        val type = FollowFollowerType.getTypeFromOrdinal(tmpType)

        val adapter = FollowPagerAdapter(supportFragmentManager, connectionInfo, userId)
        follow_follower_pageer.offscreenPageLimit = 2
        follow_follower_pageer.adapter = adapter
        follow_follower_tab.setupWithViewPager(follow_follower_pageer)
        follow_follower_pageer.currentItem = if(type == FollowFollowerType.FOLLOWER) 1 else 0

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
