package org.panta.misskeynest.repository.local

import org.panta.misskeynest.repository.ISharedPreferenceOperator

class SettingsRepository(val pref: ISharedPreferenceOperator){

    var isNotificationEnabled: Boolean
        get() = pref.getBoolean("is_notification_enabled", true)
        set(value){
            pref.putBoolean("is_notification_enabled", value)
        }



}