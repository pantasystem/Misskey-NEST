package org.panta.misskeynest.presenter

import android.net.Uri
import org.panta.misskeynest.constant.FollowFollowerType
import org.panta.misskeynest.entity.CreateNoteProperty
import org.panta.misskeynest.entity.User
import org.panta.misskeynest.interfaces.ISharedPreferenceOperator
import org.panta.misskeynest.interfaces.MainContract
import org.panta.misskeynest.repository.remote.MyInfo
import org.panta.misskeynest.repository.remote.NoteRepository
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SettingsRepository

class MainPresenter(private val mView: MainContract.View, sharedOperator: ISharedPreferenceOperator) : MainContract.Presenter{

    private lateinit var mUser: User
    private val secretRepository = PersonalRepository(sharedOperator)
    private val settingRepository = SettingsRepository(sharedOperator)

    override fun getPersonalMiniProfile() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }
        MyInfo(domain = info.domain, authKey = info.i).getMyInfo {
            if(it == null){

            }else{
                mView.showPersonalMiniProfile(it)
                mUser = it
            }

        }
    }


    override fun initDisplay() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }else{
            mView.initDisplay(info)
        }
    }
    override fun start() {
        if(settingRepository.isNotificationEnabled){
            mView.startNotificationService()
        }
    }

    override fun takeEditNote() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
        }else{
            mView.showEditNote(info)
        }
    }

    override fun getPersonalProfilePage() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }
        MyInfo(domain = info.domain, authKey = info.i).getMyInfo {
            if(it == null){

            }else{
                mView.showPersonalProfilePage(it, info)
            }
        }
    }

    override fun getFollowFollower(type: FollowFollowerType) {
        val info  = secretRepository.getConnectionInfo()

        if(info == null){
            mView.showAuthActivity()
        }else{
            mView.showFollowFollower(info, mUser, type)
        }
    }

    override fun openMisskeyOnBrowser() {
        mView.showMisskeyOnBrowser(Uri.parse(secretRepository.getConnectionInfo()?.domain))
    }

    override fun isEnabledNotification(enabled: Boolean?) {
        if(enabled == null){
            mView.showIsEnabledNotification(settingRepository.isNotificationEnabled)
            return
        }
        settingRepository.isNotificationEnabled = enabled


        if(enabled){
            mView.startNotificationService()
        }else{
            mView.stopNotificationService()
        }

    }

    override fun sendNote(text: String) {
        val info = secretRepository.getConnectionInfo()
        if(text.length > 3 && info != null){
            val note = CreateNoteProperty.Builder(i = info.i).apply {
                this.text = text
            }.create()
            NoteRepository(connectionInfo = info).send(note)
        }
    }

}