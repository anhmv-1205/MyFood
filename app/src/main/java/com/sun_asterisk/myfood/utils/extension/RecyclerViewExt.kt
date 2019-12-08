package com.sun_asterisk.myfood.utils.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun RecyclerView.onScrollListener(layoutManager: RecyclerView.LayoutManager?, swipeNotification: SwipeRefreshLayout) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (layoutManager is LinearLayoutManager) {
                if (layoutManager.childCount != 0)
                    swipeNotification.isEnabled = layoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        }
    })
}
