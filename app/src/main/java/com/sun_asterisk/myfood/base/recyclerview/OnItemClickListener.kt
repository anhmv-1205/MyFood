package com.sun_asterisk.myfood.base.recyclerview

import com.sun_asterisk.myfood.utils.Constant

interface OnItemClickListener<T> {

    fun onItemViewClick(item: T, position: Int = Constant.DEFAULT_VALUE)
}
