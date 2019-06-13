package org.panta.misskeynest.interfaces

import java.util.*

const val ONE_MINUTE = 60L * 1000
const val ONE_HOUR = ONE_MINUTE * ONE_MINUTE
const val ONE_DAY = ONE_HOUR * 24
const val ABOUT_ONE_MONTH = ONE_DAY * 30
const val ABOUT_ONE_YEAR = ONE_DAY * 365

interface ITimeFormat {
    fun formatTime(date: Date): String
}