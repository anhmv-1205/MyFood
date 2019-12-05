package com.sun_asterisk.myfood.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var swipeEnabled = false

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (swipeEnabled) {
            true -> super.onTouchEvent(ev)
            false -> false
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return when (swipeEnabled) {
            true -> return super.onInterceptTouchEvent(ev)
            else -> false
        }
    }

    fun setSwipePagingEnabled(isSwipeAble: Boolean) {
        swipeEnabled = isSwipeAble
    }
}
