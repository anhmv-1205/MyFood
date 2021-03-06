package com.sun_asterisk.myfood.utils.extension

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.utils.annotation.OrderStatus

fun TextView.setStatusOfOrder(status: String) {
    this.text = when (status) {
        OrderStatus.REQUESTING -> resources.getString(R.string.text_order_status_requesting)
        OrderStatus.APPROVED -> resources.getString(R.string.text_order_status_approved)
        OrderStatus.CANCELED -> resources.getString(R.string.text_order_status_canceled)
        OrderStatus.DONE -> resources.getString(R.string.text_order_status_done)
        OrderStatus.REJECTED -> resources.getString(R.string.text_order_status_rejected)
        else -> ""
    }
}

fun TextView.setStatusTextColor(status: String?) {
    val color: Int = when (status?.trim()) {
        OrderStatus.REQUESTING -> R.color.colorRed500
        OrderStatus.APPROVED -> R.color.colorBlue500
        OrderStatus.CANCELED -> R.color.colorGrey700
        OrderStatus.DONE -> R.color.colorGreen500
        OrderStatus.REJECTED -> R.color.colorGrey700
        else -> R.color.colorTextLogo
    }
    setTextColor(ContextCompat.getColor(context, color))
}
