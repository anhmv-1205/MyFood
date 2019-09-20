package com.sun_asterisk.myfood.common

import android.app.AlertDialog
import android.content.Context
import com.sun_asterisk.myfood.R

fun showGeneralError(from: Context) {
    AlertDialog.Builder(from)
        .setTitle("Oops")
        .setMessage("Something went wrong!")
        .setPositiveButton(from.getString(R.string.general_positive_button)) { dialog, _ -> dialog.dismiss() }
        .show()
}
