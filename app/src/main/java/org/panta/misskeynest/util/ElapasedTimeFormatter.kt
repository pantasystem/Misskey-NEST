package org.panta.misskeynest.util

import org.panta.misskeynest.interfaces.ITimeFormat
import org.panta.misskeynest.interfaces.ONE_DAY
import org.panta.misskeynest.interfaces.ONE_HOUR
import org.panta.misskeynest.interfaces.ONE_MINUTE
import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT_MINUTE = "s"
const val DATE_FORMAT_HOUR = "H"
const val DATE_FORMAT_DAY = "D"
const val DATE_FORMAT_MONTH = "M"
const val DATE_FORMAT_YEAR = ""


class ElapasedTimeFormatter : ITimeFormat{

    override fun formatTime(date: Date): String {
        val nowTime = Date()

        val timeLag = nowTime.time - date.time
        return when{
            timeLag < ONE_MINUTE -> {
                val d = SimpleDateFormat(DATE_FORMAT_MINUTE, Locale.getDefault()).format(Date(timeLag))
                "${d}秒前"
            }
            timeLag < ONE_HOUR -> {
                val d = SimpleDateFormat(DATE_FORMAT_HOUR, Locale.getDefault()).format(Date(timeLag))
                "${d}時間前"
            }
            timeLag < ONE_DAY -> {
                val d = SimpleDateFormat(DATE_FORMAT_DAY, Locale.getDefault()).format(Date(timeLag))
                "${d}日前"
            }
            else -> "かなり昔"
        }
    }
}