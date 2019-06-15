package org.panta.misskeynest.view

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

abstract class AbsBaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val themePref = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        val isNightMode = themePref.getBoolean("isNightMode", false)
        Log.d(this.javaClass.name, "isNightMode: $isNightMode")

    }

    fun putTheme(isNightMode: Boolean){
        val themePref = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        val edit =themePref.edit()
        edit.putBoolean("isNightMode", isNightMode)
        edit.apply()
    }

    fun isNightMode(): Boolean{
        val themePref = getSharedPreferences(packageName + "_preferences", Context.MODE_PRIVATE)
        return themePref.getBoolean("isNightMode", false)
    }
}