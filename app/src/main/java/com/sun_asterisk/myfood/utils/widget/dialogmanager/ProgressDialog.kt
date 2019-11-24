package com.sun_asterisk.myfood.utils.widget.dialogmanager

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.sun_asterisk.myfood.R

class ProgressDialog(context: Context) : Dialog(context) {

    init {
        initView()
    }

    private fun initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.item_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }
}
