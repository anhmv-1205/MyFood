package com.sun_asterisk.myfood.utils.extension

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.utils.listener.OnDataCalendarListener
import java.util.Calendar

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setState(isVisible: Boolean) {
    if (isVisible) show() else hide()
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun View.showSnackBar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()

fun Context.showDatePickerDialog(calendar: Calendar = Calendar.getInstance(), listener: OnDataCalendarListener) {
    val yyyy = calendar.get(Calendar.YEAR)
    val mm = calendar.get(Calendar.MONTH)
    val dd = calendar.get(Calendar.DAY_OF_MONTH)
    val datePicker = DatePickerDialog(
        this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val timeStr = "$year-" + (month + 1) + "-$dayOfMonth"
            listener.onDataSet(timeStr)
        }, yyyy, mm, dd
    )
    datePicker.show()
}

fun View.fadeOutWithAnimation() {
    val animate = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    this.startAnimation(animate)
    this.gone()
}
