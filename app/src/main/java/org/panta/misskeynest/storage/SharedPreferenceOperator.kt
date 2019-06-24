package org.panta.misskeynest.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.panta.misskeynest.interfaces.ISharedPreferenceOperator

class SharedPreferenceOperator(private val context: Context) : ISharedPreferenceOperator{

    companion object {
        private const val APP_NAME = "misskey_for_Android_pref_version_2.0"
    }
    override fun getString(key: String, defaultValue: String?): String?{
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getString(key, null)
    }

    override fun putString(key: String, value: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}