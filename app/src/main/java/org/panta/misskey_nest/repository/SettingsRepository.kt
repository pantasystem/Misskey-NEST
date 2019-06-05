package org.panta.misskey_nest.repository

import android.content.Context
import org.panta.misskey_nest.interfaces.ISharedPreferenceOperator
import org.panta.misskey_nest.storage.SharedPreferenceOperator

class SettingsRepository(val pref: ISharedPreferenceOperator){

    var isNotificationEnabled: Boolean
        get() = pref.getBoolean("is_notification_enabled", true)
        set(value){
            pref.putBoolean("is_notification_enabled", value)
        }



}