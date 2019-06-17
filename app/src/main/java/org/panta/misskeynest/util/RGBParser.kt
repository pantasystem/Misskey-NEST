package org.panta.misskeynest.util

import java.util.regex.Pattern

fun parseRGBv11(rgb: Any): List<Int>?{

    var rgbList: List<Int>? = null
    val pattern = Pattern.compile("\\d")
    if( rgb is String ){
        rgbList = ArrayList<Int>()

        val list = rgb.split(",")
        list.forEach{
            val numberBuilder = StringBuilder()
            val matcher = pattern.matcher(it)
            while(matcher.find()){
                numberBuilder.append(matcher.group())
            }
            val number = Integer.parseInt(numberBuilder.toString())
            rgbList.add(number)
        }

    }
    return rgbList
}