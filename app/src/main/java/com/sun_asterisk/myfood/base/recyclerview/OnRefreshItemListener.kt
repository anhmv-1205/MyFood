package com.sun_asterisk.myfood.base.recyclerview

interface OnRefreshItemListener<T> {
    fun onCreateItem(item: T)

    fun onUpdateItem(item: T)
}
