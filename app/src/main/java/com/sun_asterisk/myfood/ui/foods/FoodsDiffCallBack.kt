package com.sun_asterisk.myfood.ui.foods

import androidx.recyclerview.widget.DiffUtil
import com.sun_asterisk.myfood.data.model.Food

class FoodsDiffCallBack(
    private val oldList: MutableList<Food>,
    private val newList: MutableList<Food>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFood = oldList[oldItemPosition]
        val newFood = newList[newItemPosition]
        return oldFood.name == newFood.name
            && oldFood.state == newFood.state
            && oldFood.amountBuy == newFood.amountBuy
            && oldFood.cost == newFood.cost
            && oldFood.unitFood == newFood.unitFood
            && oldFood.imgUrl == newFood.imgUrl
    }
}
