package com.sun_asterisk.myfood.utils.extension

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import com.sun_asterisk.myfood.R
import kotlinx.android.synthetic.main.layout_normal_dialog.view.textViewButtonLine
import kotlinx.android.synthetic.main.layout_normal_dialog.view.textViewCancel
import kotlinx.android.synthetic.main.layout_normal_dialog.view.textViewConfirm
import kotlinx.android.synthetic.main.layout_normal_dialog.view.textViewMessage

private const val VALUE_MAX_TIME_CLICK = 1000
private var lastClickTime: Long = 0

fun isMultiClick(time: Int = VALUE_MAX_TIME_CLICK): Boolean {
    if (SystemClock.elapsedRealtime() - lastClickTime < time)
        return true
    lastClickTime = SystemClock.elapsedRealtime()
    return false
}

fun Context.showAlertDialogBasic(
    message: String? = null,
    isShowCancelButton: Boolean = true,
    isCancelable: Boolean = true,
    isTextGravityCenter: Boolean = false,
    func: (() -> Unit)
) {
    var dialog: AlertDialog? = null
    val view = LayoutInflater.from(this).inflate(R.layout.layout_normal_dialog, null)
    if (isTextGravityCenter) view.textViewMessage.gravity = Gravity.CENTER_HORIZONTAL
    view.textViewMessage.text = message
    view.textViewConfirm.setOnClickListener {
        func()
        dialog?.dismiss()
    }
    if (isShowCancelButton) view.textViewCancel.setOnClickListener { dialog?.dismiss() }
    else {
        view.textViewCancel.gone()
        view.textViewButtonLine.gone()
    }
    dialog = AlertDialog.Builder(this)
        .setCancelable(isCancelable)
        .setView(view)
        .create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
}
