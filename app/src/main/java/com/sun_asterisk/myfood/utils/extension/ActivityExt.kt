package com.sun_asterisk.myfood.utils.extension

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sun_asterisk.myfood.utils.AnimateType

fun AppCompatActivity.addFragmentToActivity(
    @IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName
    , animateType: AnimateType? = AnimateType.FADE
) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}
