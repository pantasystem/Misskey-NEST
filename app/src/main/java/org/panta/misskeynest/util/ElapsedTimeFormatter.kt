package org.panta.misskeynest.util

import org.panta.misskeynest.interfaces.*
import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT_SECONDS = "s"
const val DATE_FORMAT_MINUTE = "m"
const val DATE_FORMAT_HOUR = "H"
const val DATE_FORMAT_DAY = "D"
const val DATE_FORMAT_MONTH = "M"
const val DATE_FORMAT_YEAR = ""


class ElapsedTimeFormatter : ITimeFormat{

    override fun formatTime(date: Date): String {
        val nowTime = Date()

        val timeLag = (nowTime.time - date.time)
        val tmp =  when{
            timeLag < ONE_MINUTE -> {
                val d = SimpleDateFormat(DATE_FORMAT_SECONDS, Locale.getDefault()).format(Date(timeLag))
                "${d}秒前"
            }
            timeLag < ONE_HOUR-> {
                val d = SimpleDateFormat(DATE_FORMAT_MINUTE, Locale.getDefault()).format(Date(timeLag))
                "${d}分前"
            }
            timeLag < ONE_DAY -> {
                val d = SimpleDateFormat(DATE_FORMAT_HOUR, Locale.getDefault()).format(Date(timeLag))
                "${d}時間前"
            }
            timeLag < ABOUT_ONE_MONTH -> {
                val d = SimpleDateFormat(DATE_FORMAT_DAY, Locale.getDefault()).format(Date(timeLag))
                "${d}日前"
            }
            else -> "かなり昔"
        }
        return tmp
    }

}