package org.panta.misskeynest.repository

import android.content.Context
import org.panta.misskeynest.interfaces.ISharedPreferenceOperator
import org.panta.misskeynest.storage.SharedPreferenceOperator

class SettingsRepository(val pref: ISharedPreferenceOperator){

    var isNotificationEnabled: Boolean
        get() = pref.getBoolean("is_notification_enabled", true)
        set(value){
            pref.putBoolean("is_notification_enabled", value)
        }



}