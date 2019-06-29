package org.panta.misskeynest

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.twitter.TwitterEmojiProvider
import kotlinx.android.synthetic.main.activity_auth.*
import org.panta.misskeynest.constant.DomainAndAppSecret
import org.panta.misskeynest.constant.getInstanceInfoList
import org.panta.misskeynest.entity.SessionResponse
import org.panta.misskeynest.contract.AuthContract
import org.panta.misskeynest.interfaces.ItemClickListener
import org.panta.misskeynest.repository.local.SharedPreferenceOperator
import org.panta.misskeynest.presenter.AuthPresenter

class AuthActivity : AppCompatActivity(), AuthContract.View {

    override var mPresenter: AuthContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(TwitterEmojiProvider())

        setContentView(R.layout.activity_auth)
        //val sp  = SharedPreferenceOperator(getSharedPreferences("tokenData", Context.MODE_PRIVATE))

        val sharedPref = SharedPreferenceOperator(this)

        //FIXME 不具合の原因


        if(Intent.ACTION_VIEW == intent.action){
            mPresenter = AuthPresenter(this, sharedPref, null, null)

            Log.d("AuthActivity", "呼び出された")

            mPresenter?.getUserToken()
        }else{
            val instanceList = getInstanceInfoList()
            val defaultInfo = instanceList[0]
            mPresenter = AuthPresenter(
                this,
                sharedPref,
                defaultInfo.domain,
                defaultInfo.appSecret
            )

            val lm = LinearLayoutManager(this)
            val adapter = org.panta.misskeynest.adapter.InstanceListAdapter(instanceList)
            instance_list_view.layoutManager = lm
            instance_list_view.adapter = adapter

            val c = this
            adapter.clickListener = object : ItemClickListener<DomainAndAppSecret> {
                override fun onClick(e: DomainAndAppSecret) {
                    mPresenter =
                        AuthPresenter(c, sharedPref, e.domain, e.appSecret)
                }
            }
        }

        auth_button.setOnClickListener{
            mPresenter?.getSession()
        }

    }

    override fun onLoadSession(session: SessionResponse) {
        this.showBrowser(Uri.parse(session.url))
    }

    override fun onLoadUserToken(token: String, domain: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)

        startActivity(intent)
    }

    override fun showBrowser(uri: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }



}
