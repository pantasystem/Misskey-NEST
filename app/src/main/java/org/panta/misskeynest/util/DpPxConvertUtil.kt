package org.panta.misskeynest.util

import android.content.Context
import android.util.DisplayMetrics


fun convertDp2Px(dp: Float, context: Context): Float {
    val metrics = context.resources.displayMetrics
    return dp * metrics.density
}


fun convertPx2Dp(px: Int, context: Context): Float {
    val metrics = context.resources.displayMetrics
    return px / metrics.density
}