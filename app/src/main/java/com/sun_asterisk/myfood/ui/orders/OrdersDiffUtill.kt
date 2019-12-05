package com.sun_asterisk.myfood.ui.orders

import androidx.recyclerview.widget.DiffUtil
import com.sun_asterisk.myfood.data.model.Order

class OrdersDiffUtill(
    private val oldList: MutableList<Order>,
    private val newList: MutableList<Order>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.status == newItem.status
    }
}
