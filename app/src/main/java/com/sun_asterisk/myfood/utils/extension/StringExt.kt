package com.sun_asterisk.myfood.utils.extension

import android.text.TextUtils
import android.util.Patterns
import com.sun_asterisk.myfood.utils.Constant
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

fun String.compareTimeWithCurrent(shift: String): Boolean {
    val parsedDate = SimpleDateFormat(Constant.DATETIME_FORMAT_YYYY_MM_DD, Locale.US).parse(this)
    val current = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    if (parsedDate == current.time) {
        val compare = Calendar.getInstance()
        val hourOfDay = compare.get(Calendar.HOUR_OF_DAY)
        return if (shift == Constant.SHIFT_AM) (Constant.SHIFT_AM_HOUR_RULE - hourOfDay) > 0
        else (Constant.SHIFT_PM_HOUR_RULE - hourOfDay) > 0
    }
    return parsedDate > current.time
}

fun String.toDateWithFormat(inputFormat: String, outputFormat: String): String {
    val gmtTimeZone = TimeZone.getTimeZone("UTC")
    val inputDateTimeFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
    inputDateTimeFormat.timeZone = gmtTimeZone

    val outputDateTimeFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
    outputDateTimeFormat.timeZone = gmtTimeZone
    return try {
        outputDateTimeFormat.format(inputDateTimeFormat.parse(this))
    } catch (ex: ParseException) {
        this
    }
}

@Throws(ParseException::class)
fun String.toDate(format: String): Date {
    val parser = SimpleDateFormat(format, Locale.getDefault())
    return parser.parse(this)
}

fun String.createCalendarWithFormat(format: String = Constant.DATETIME_FORMAT_YYYY_MM_DD): Calendar {
    val date = this.toDate(format)
    return Calendar.getInstance().apply { time = date }
}

fun String.toRequestBodyImageType(partName: String = Constant.PART_FILE): MultipartBody.Part {
    val file = File(this)
    val requestBody = file.asRequestBody("image/*".toMediaType())
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}

fun String.toRequestBodyTextType(): RequestBody {
    return this.toRequestBody("text/plain".toMediaType())
}
