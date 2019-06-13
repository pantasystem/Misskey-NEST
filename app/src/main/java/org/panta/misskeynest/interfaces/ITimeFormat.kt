package org.panta.misskeynest.interfaces

import java.util.*

const val ONE_MINUTE = 60L
const val ONE_HOUR = 60L * 60L
const val ONE_DAY = 60L * 60L * 24
const val ABOUT_ONE_MONTH = ONE_DAY * 30
const val ABOUT_ONE_YEAR = ONE_DAY * 365

interface ITimeFormat {
    fun formatTime(date: Date): String
}