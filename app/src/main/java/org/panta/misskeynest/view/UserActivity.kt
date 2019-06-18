package org.panta.misskeynest.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_user.*
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.repository.PersonalRepository
import org.panta.misskeynest.repository.UserRepository
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.view.user.UserPagerAdapter
import org.panta.misskeynest.view.user_auth.AuthActivity
import java.lang.IllegalArgumentException

class UserActivity : AppCompatActivity() {

    companion object{
        private const val USER_PROPERTY_TAG = "UserActivityUserPropertyTag"
        //private const val CONNECTION_INFO = "UserActivityConnectionInfo"

        fun startActivity(context: Context?, user: User){
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(USER_PROPERTY_TAG, user)
            context?.startActivity(intent)
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(TwitterEmojiProvider())
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        val intent = intent
        val tmpUser: User? = intent.getSerializableExtra(USER_PROPERTY_TAG) as User
        val info = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()
        if(info == null){
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }
        if(tmpUser == null){
            finish()
            throw IllegalArgumentException("user propertyがNULL")
        }

        UserRepository(info.domain, info.i).getUserInfo(tmpUser.id){
            if(it == null) return@getUserInfo
            runOnUiThread{
                Picasso.get().load(it.avatarUrl).into(profile_icon)

                profile_user_id.text = if(it.host == null){
                    "@${it.userName}"
                }else{
                    "@${it.userName}@${it.host}"
                }
                profile_user_name.text = it.name ?: it.userName
                profile_description.text = it.description
                profile_follow_count.text = "${it.followingCount} フォロー"
                profile_follower_count.text = "${it.followersCount} フォロワー"
                posts_count.text = "${it.notesCount} 投稿"
                profile_age.visibility = View.GONE
                profile_follow_count.setOnClickListener{_ ->
                    FollowFollowerActivity.startActivity(applicationContext, FollowFollowerType.FOLLOWING, it.id)
                }
                profile_follower_count.setOnClickListener{_ ->
                    FollowFollowerActivity.startActivity(applicationContext, FollowFollowerType.FOLLOWER, it.id)
                }

                Picasso.get().load(it.bannerUrl).into(header_image)
            }

        }

        val ad = profile_view_pager.adapter
        val adapter = if(ad == null){
            UserPagerAdapter(supportFragmentManager, tmpUser.id, info)
        }else{
            ad.notifyDataSetChanged()
            ad
        }
        profile_view_pager.offscreenPageLimit = 3
        profile_view_pager.adapter = adapter

        profile_tab.setupWithViewPager(profile_view_pager)



    }

    private fun showFollowList(info: ConnectionProperty, user: User, type: FollowFollowerType){
        val intent = Intent(applicationContext, FollowFollowerActivity::class.java)
        intent.putExtra(FollowFollowerActivity.FOLLOW_FOLLOWER_TYPE, type)
        intent.putExtra(FollowFollowerActivity.USER_ID_TAG, user.id)
    }

}
