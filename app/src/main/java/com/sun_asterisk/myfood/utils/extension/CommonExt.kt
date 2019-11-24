package com.sun_asterisk.myfood.utils.extension

import android.os.SystemClock

private const val VALUE_MAX_TIME_CLICK = 1000
private var lastClickTime: Long = 0

fun isCheckMultiClick(time: Int = VALUE_MAX_TIME_CLICK): Boolean {
    if (SystemClock.elapsedRealtime() - lastClickTime < time)
        return false
    lastClickTime = SystemClock.elapsedRealtime()
    return true
}
