package org.panta.misskeynest.view_presenter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.ConnectionProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.ISharedPreferenceOperator
import org.panta.misskeynest.interfaces.MainContract
import org.panta.misskeynest.service.EmojiDownloadService
import org.panta.misskeynest.service.NotificationService
import org.panta.misskeynest.storage.SharedPreferenceOperator
import org.panta.misskeynest.view_presenter.follow_follower.FollowFollowerActivity
import org.panta.misskeynest.view_presenter.mixed_timeline.PagerAdapter
import org.panta.misskeynest.view_presenter.note_editor.EditNoteActivity
import org.panta.misskeynest.view_presenter.test_ui.TestActivity
import org.panta.misskeynest.view_presenter.user.UserActivity
import org.panta.misskeynest.view_presenter.user_auth.AuthActivity

private const val FRAGMENT_HOME = "FRAGMENT_HOME"
private const val FRAGMENT_OTHER = "FRAGMENT_OTHER"
//const val DOMAIN_AUTH_KEY_TAG = "MainActivityUserDomainAndAuthKey"


class MainActivity : AbsBaseActivity(), NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    companion object {
        const val SHOW_FRAGMENT_TAG = "MainActivityShowFragmentTag"
        const val HOME = 0
        const val MIX = 1
        const val NOTIFICATION = 2
    }

    //一番目に表示されるフラグメント
    private var showFirstFragment = 0

    override var mPresenter: MainContract.Presenter? = null
    //private var i: String? = null
    //private var domain: String? = null
    private val mSharedOperator: ISharedPreferenceOperator by lazy{
        SharedPreferenceOperator(this)
    }

    private lateinit var mNotificationEnabledSwitch: Switch

    //private lateinit var mUiModeManager: UiModeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LeakCanary.install(application)
        mPresenter = MainPresenter(this, mSharedOperator)
        //setThemeFromType(this)
        if(super.isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        EmojiManager.install(TwitterEmojiProvider())
        
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        /*~~basic init*/

        showFirstFragment = intent.getIntExtra(SHOW_FRAGMENT_TAG, 0)


        fab.setOnClickListener {
            mPresenter?.takeEditNote()
        }
        mPresenter?.start()

        mPresenter?.getPersonalMiniProfile()

        mPresenter?.initDisplay()


        title = "ホーム"

        val header = nav_view.getHeaderView(0)
        header.following_text.setOnClickListener {
            mPresenter?.getFollowFollower(FollowFollowerType.FOLLOWING)
        }

        header.follower_text.setOnClickListener{
            mPresenter?.getFollowFollower(FollowFollowerType.FOLLOWER)
        }


        val customView = nav_view.menu.findItem(R.id.nav_notification).actionView
        mNotificationEnabledSwitch = customView.findViewById(R.id.switch_button)
        mNotificationEnabledSwitch.setOnCheckedChangeListener { _, b ->
            mPresenter?.isEnabledNotification(b)
        }
        mPresenter?.isEnabledNotification()

        //mUiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        simple_send_button.setOnClickListener{
            val text = simple_edit_box.text.toString()
            mPresenter?.sendNote(text)
            simple_edit_box.text.clear()
        }




    }


    override fun initDisplay(connectionInfo: ConnectionProperty) {

        //val ad = timeline_pager.adapter
        val adapter = PagerAdapter(supportFragmentManager, connectionInfo)

        timeline_pager.offscreenPageLimit = 4
        timeline_pager.adapter = adapter

        timeline_tab_layout.setupWithViewPager(timeline_pager)

        timeline_tab_layout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    if(p0 != null){
                        val fragment = adapter.getFragment(p0.position)
                        fragment.mPresenter?.initTimeline()
                    }
                }
                override fun onTabSelected(p0: TabLayout.Tab?) {
                }
                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }
            }
        )
        Log.d("", "tabCount: ${timeline_tab_layout.tabCount}!!!!!!!!!!!!!!!!!!!!!!!")

        //tab_layoutのアイコンを設定
        for(n in 0.until(timeline_tab_layout.tabCount)){
            val tab = timeline_tab_layout.getTabAt(n)

            when(n){
                0 ->{
                    tab?.setIcon(R.drawable.ic_home)
                    tab?.contentDescription = "Home"
                }
                1 ->{
                    tab?.setIcon(R.drawable.ic_local)
                    tab?.contentDescription = "Local"
                }
                2 ->{
                    tab?.setIcon(R.drawable.ic_social)
                    tab?.contentDescription = "Social"
                }
                3 ->{
                    tab?.setIcon(R.drawable.ic_global)
                    tab?.contentDescription = "Global"
                }

            }
            //timeline_tab_layout.addTab(tab!!)
        }

    }

    override fun showAuthActivity() {
        runOnUiThread{
            val intent = Intent(applicationContext, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showPersonalMiniProfile(user: User) {
        runOnUiThread {
            if(user.avatarUrl != null && my_account_icon != null){
                Picasso
                    .get()
                    .load(user.avatarUrl)
                    .into(my_account_icon)
            }

            my_name?.text = user.name?: user.userName

            val userName = if(user.host != null){
                "@${user.userName}@${user.host}"
            }else{
                "@${user.userName}"
            }
            my_user_name?.text = userName

            follower_count?.text = user.followersCount.toString()
            following_count?.text= user.followingCount.toString()

        }
    }

    override fun showPersonalProfilePage(user: User, connectionInfo: ConnectionProperty) {
        UserActivity.startActivity(applicationContext, user)
    }

    override fun showEditNote(connectionInfo: ConnectionProperty) {
        val intent = Intent(applicationContext, EditNoteActivity::class.java)
        //intent.putExtra(EditNoteActivity.CONNECTION_INFO, info)
        startActivity(intent)
    }

    override fun showFollowFollower(connectionInfo: ConnectionProperty, user: User, type: FollowFollowerType) {
        FollowFollowerActivity.startActivity(applicationContext, type, user.id)
    }

    override fun showMisskeyOnBrowser(url: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }

    override fun showIsEnabledNotification(enabled: Boolean) {
        Log.d("MainActivity", "選択中 $enabled")
        mNotificationEnabledSwitch.isChecked = enabled
    }

    override fun startNotificationService() {
        startService(Intent(applicationContext, NotificationService::class.java))
        Toast.makeText(applicationContext, "通知はONです", Toast.LENGTH_SHORT).show()
    }

    override fun stopNotificationService() {
        stopService(Intent(applicationContext, NotificationService::class.java))
        Toast.makeText(applicationContext, "通知がOFFになりました", Toast.LENGTH_SHORT).show()
    }

    private fun setFragment(fragment: Fragment, fragmentName: String){
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.main_container, fragment)

        val count = fm.backStackEntryCount

        if(fragmentName == FRAGMENT_OTHER){
            ft.addToBackStack(fragmentName)
        }
        ft.commit()

        fm.addOnBackStackChangedListener( object : android.support.v4.app.FragmentManager.OnBackStackChangedListener{
            override fun onBackStackChanged() {
                if(fm.backStackEntryCount <= count){
                    fm.popBackStack(FRAGMENT_OTHER, POP_BACK_STACK_INCLUSIVE)
                    fm.removeOnBackStackChangedListener(this)
                    //bottom_navigation.menu.getItem(0).isChecked = true
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()

        Log.d("MainActivity", "あああああああああああああああああああああああああああonStartが呼び出された")
        mPresenter?.getPersonalMiniProfile()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(Intent(applicationContext, EmojiDownloadService::class.java))
        }else{
            startService(Intent(applicationContext, EmojiDownloadService::class.java))
        }

    }

    override fun onBackPressed() {

        //NavigationDrawerが開いているときに戻るボタンを押したときの動作
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        }

        if(timeline_tab_layout.selectedTabPosition == 0){
            super.onBackPressed()
        }else{
            timeline_tab_layout.getTabAt(0)?.select()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> mPresenter?.getPersonalProfilePage()
            R.id.nav_notification -> startActivity(Intent(this, NotificationActivity::class.java))
            R.id.nav_search -> startActivity(Intent(this, SearchActivity::class.java))
            R.id.nav_open_web_misskey -> mPresenter?.openMisskeyOnBrowser()
            R.id.nav_open_test -> startActivity(Intent(this, TestActivity::class.java))
            R.id.nav_ui_mode -> {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                if(super.isNightMode()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    super.putTheme(false)

                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    super.putTheme(true)
                    //mUiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
                    //mUiModeManager.nightMode = UiModeManager.MODE_NIGHT_YES

                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return false
    }


}
