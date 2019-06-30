package org.panta.misskeynest.util

import android.content.Context
import org.panta.misskeynest.R
import org.panta.misskeynest.constant.ThemeType
import org.panta.misskeynest.repository.local.PersonalRepository
import org.panta.misskeynest.repository.local.SharedPreferenceOperator

fun setThemeFromType(context: Context){
    val type = PersonalRepository(SharedPreferenceOperator(context)).getUserTheme()
    when(ThemeType.STANDARD){
        ThemeType.STANDARD -> context.setTheme(R.style.AppTheme)
    }
}