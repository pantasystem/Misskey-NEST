package org.panta.misskeynest.util

import android.graphics.Color

fun getTextContrastFromRGB(rgb: List<Int>): Int{
    if(rgb.size > 3) throw IllegalAccessException("要素数はRGBの三つまでです。RGB(255,100,255)なら[255,100,255]とします。")

    val red = rgb[0]
    val green = rgb[1]
    val blue = rgb[2]

    val relativeLuminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue

    val tmpBlackContrast = ( 1 + 0.05 ) / ( relativeLuminance + 0.05 )
    val tmpWhiteContrast = ( relativeLuminance + 0.05 ) / ( 1 + 0.05 )

    return if( tmpBlackContrast > tmpWhiteContrast ){
        Color.BLACK
    }else{
        Color.WHITE
    }
}