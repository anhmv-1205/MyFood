package com.sun_asterisk.myfood.utils.widget.dialogmanager

interface DialogManager {

    fun showLoading()

    fun hideLoading()

    fun setState(isVisible: Boolean)

    fun onRelease()
}
