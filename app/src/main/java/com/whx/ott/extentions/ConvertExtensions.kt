package com.whx.ott.extentions

import android.content.Context
import java.util.*

/**
 * Created by oleky on 2017/11/3.
 */
fun Context.generateTime(position: Long): String {
    val totalSeconds = position / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}