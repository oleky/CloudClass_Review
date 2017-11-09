package com.whx.ott.extentions

import android.content.Context
import android.util.TypedValue
import android.widget.Toast


/**
 * Created by oleky on 2017/11/2.
 */

fun Context.dp2sp(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.applicationContext.resources.displayMetrics).toInt()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}