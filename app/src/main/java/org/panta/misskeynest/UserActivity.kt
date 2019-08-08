package org.panta.misskeynest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_user.*
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.pager.UserPagerAdapter
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.repository.remote.UserRepository

class UserActivity : AppCompatActivity() {

    companion object{
        private const val USER_PROPERTY_TAG = "UserActivityUserPropertyTag"
        //private const val CONNECTION_INFO = "UserActivityConnectionInfo"

        fun getIntent(context: Context?, user: User): Intent{
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(USER_PROPERTY_TAG, user)
            return intent
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(TwitterEmojiProvider())
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        val info = PersonalRepository(SharedPreferenceOperator(this)).getConnectionInfo()

        //暗黙なIntentの場合uriはnullになる
        val uri = intent.data
        val userName = uri?.getQueryParameter("userId")
        Log.d("UserActivity", "uriは: $uri, query: $userName")

        if(userName != null){
            UserRepository(info!!.domain, info.i).getUserInfoByUserName(userName){
                Handler(Looper.getMainLooper()).post{
                    setUserOnView(it)
                    if(it!=null){
                        setUserPagerAdapter(it, info)
                    }
                }

            }
        }else{
            val intent = intent
            val tmpUser: User? = intent.getSerializableExtra(USER_PROPERTY_TAG) as User


            if(info == null){
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                return
            }
            if(tmpUser == null){
                finish()
                throw IllegalArgumentException("user propertyがNULL")
            }
            setUserPagerAdapter(tmpUser, info)

            UserRepository(info.domain, info.i).getUserInfo(tmpUser.id){
                setUserOnView(it)
                if(it!= null){
                }
            }
        }



        /*




*/


    }

    @SuppressLint("SetTextI18n")
    private fun setUserOnView(user: User?){
        if(user == null) return
        runOnUiThread{
            Picasso.get().load(user.avatarUrl).into(profile_icon)

            profile_user_id.text = if(user.host == null){
                "@${user.userName}"
            }else{
                "@${user.userName}@${user.host}"
            }
            profile_user_name.text = user.name ?: user.userName
            profile_description.text = user.description
            profile_follow_count.text = "${user.followingCount} フォロー"
            profile_follower_count.text = "${user.followersCount} フォロワー"
            posts_count.text = "${user.notesCount} 投稿"
            profile_age.visibility = View.GONE
            profile_follow_count.setOnClickListener{_ ->
                startActivity(
                    FollowFollowerActivity.getIntent(
                        applicationContext,
                        FollowFollowerType.FOLLOWING,
                        user.id
                    )
                )
            }
            profile_follower_count.setOnClickListener{_ ->
                startActivity(
                    FollowFollowerActivity.getIntent(
                        applicationContext,
                        FollowFollowerType.FOLLOWER,
                        user.id
                    )
                )
            }

            Picasso.get().load(user.bannerUrl).into(header_image)
        }
    }

    private fun setUserPagerAdapter(user: User, info: ConnectionProperty){
        val ad = profile_view_pager.adapter
        val adapter = if(ad == null){
            UserPagerAdapter(supportFragmentManager, user, info)
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
