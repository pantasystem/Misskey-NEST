package org.panta.misskey_nest.util

import android.content.Context
import org.panta.misskey_nest.R
import org.panta.misskey_nest.constant.ThemeType
import org.panta.misskey_nest.repository.PersonalRepository
import org.panta.misskey_nest.storage.SharedPreferenceOperator

fun setThemeFromType(context: Context){
    val type = PersonalRepository(SharedPreferenceOperator(context)).getUserTheme()
    when(ThemeType.STANDARD){
        ThemeType.STANDARD -> context.setTheme(R.style.AppTheme)
    }
}