package com.sun_asterisk.myfood.ui.main

import androidx.appcompat.widget.Toolbar

interface OnActionBarListener {
    fun setupActionBar(toolbar: Toolbar, isShowBackIcon: Boolean = true)
}
