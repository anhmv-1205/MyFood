package com.sun_asterisk.myfood.utils.extension

import android.text.TextUtils
import android.util.Patterns
import com.sun_asterisk.myfood.utils.Constant
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun List<String>.toStringWithFormatPattern(format: String): String {
    if (this.isEmpty()) {
        return ""
    }
    val builder = StringBuilder()
    for (s in this) {
        builder.append(s)
        builder.append(format)
    }
    var result = builder.toString()
    result = result.substring(0, result.length - format.length)
    return result
}

fun String.replaceIpAddress(
    oldIp: String = Constant.LOCAL_HOST,
    newIp: String = Constant.IP,
    ignoreCase: Boolean = true
) =
    this.replace(oldIp, newIp, ignoreCase)

fun String.addDistanceUnits(unit: String = Constant.KILOMETER): String {
    return "$this $unit"
}

fun String.validateItemDuration(
    numberOfDay: Int,
    formatType: String = Constant.DATETIME_FORMAT_YYYY_MM_DD
): Boolean {
    val parsedDate = SimpleDateFormat(formatType, Locale.ENGLISH).parse(this)
    val current = Calendar.getInstance().time
    if (current.after(parsedDate)) {
        val differenceInTime = current.time - parsedDate.time
        val differenceInDate = differenceInTime / (1000 * 3600 * 24)
        return differenceInDate <= numberOfDay
    }
    return false
}

fun String.isEmailValid() = !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

