package com.sun_asterisk.myfood.utils.extension

import com.sun_asterisk.myfood.utils.Constant

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

fun String.replaceIpAddress(oldIp: String = Constant.LOCAL_HOST, newIp: String = Constant.IP, ignoreCase: Boolean = true) =
    this.replace(oldIp, newIp, ignoreCase)
