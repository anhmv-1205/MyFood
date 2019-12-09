package com.sun_asterisk.myfood.utils.extension

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.utils.AnimateType
import com.sun_asterisk.myfood.utils.AnimateType.FADE

fun Fragment.replaceFragment(
    @IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = FADE
) {
    fragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = FADE
) {
    fragmentManager?.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.addChildFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType? = FADE
) {
    childFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun Fragment.goBackFragment(): Boolean {
    fragmentManager.notNull {
        val isShowPreviousPage = it.backStackEntryCount > 0
        if (isShowPreviousPage) {
            it.popBackStackImmediate()
        }
        return isShowPreviousPage
    }
    return false
}

fun Fragment.comebackHomeFragment() {
    fragmentManager?.notNull { fm ->
        repeat((1..fm.backStackEntryCount).count()) { fm.popBackStack() }
    }
}

fun Fragment.showChildFragment(@IdRes containerId: Int, vararg hideFragments: Fragment, showFragment: Fragment) {
    val existFragment = childFragmentManager.findFragmentByTag(showFragment.tag)
    childFragmentManager.beginTransaction().apply {
        hideFragments.forEach { hide(it) }
        if (existFragment != null) show(existFragment)
        else addChildFragment(containerId, showFragment)
        commit()
    }
}

fun Fragment.addFragmentToActivity(
    @IdRes containerId: Int, fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName
    , animateType: AnimateType? = FADE
) {
    (this.activity as AppCompatActivity).addFragmentToActivity(
        containerId,
        fragment,
        addToBackStack,
        tag,
        animateType
    )
}

inline fun FragmentManager.transact(
    action: FragmentTransaction.() -> Unit,
    animateType: AnimateType? = FADE
) {
    beginTransaction().apply {
        setCustomAnimations(this, animateType)
        action()
    }.commitAllowingStateLoss()
}

fun setCustomAnimations(
    transaction: FragmentTransaction,
    animateType: AnimateType? = FADE
) {
    when (animateType) {
        FADE -> transaction.setCustomAnimations(
            R.anim.fade_in, R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )
        AnimateType.SLIDE_TO_RIGHT -> transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left, R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        AnimateType.SLIDE_TO_LEFT -> transaction.setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_out_right, R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
}

fun setupDismissKeyBoard(context: Activity?, view: View?) {
    // Set up touch listener for non-text box views to hide keyboard.
    if (view !is EditText) {
        view?.setOnTouchListener { _, _ ->
            val inputMethodManager =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)

            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupDismissKeyBoard(context, innerView)
        }
    }
}

fun delayTask(func: () -> Unit, duration: Long = 1000) {
    Handler().postDelayed(func, duration)
}
