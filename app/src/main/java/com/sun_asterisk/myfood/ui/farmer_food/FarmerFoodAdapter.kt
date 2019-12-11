package com.sun_asterisk.myfood.ui.farmer_food

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.R.layout
import com.sun_asterisk.myfood.base.recyclerview.BaseRecyclerViewAdapter
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.ui.foods.FoodsDiffCallBack
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import com.sun_asterisk.myfood.utils.extension.setState
import com.sun_asterisk.myfood.utils.extension.validateItemDuration
import kotlinx.android.synthetic.main.item_food_vertical.view.imageViewFood
import kotlinx.android.synthetic.main.item_food_vertical.view.imageViewNew
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewAmountBuy
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewCost
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewFoodName
import kotlinx.android.synthetic.main.item_food_vertical.view.textViewOutOfFood

class FarmerFoodAdapter(context: Context, dataList: MutableList<Food>) :
    BaseRecyclerViewAdapter<Food, ViewHolder>(context, dataList = dataList) {

    private var lastPosition = Constant.INVALID_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM)
            FoodViewHolder(
                layoutInflater.inflate(
                    layout.item_food_vertical,
                    parent,
                    false
                ), itemClickListener
            )
        else ItemLoading(
            layoutInflater.inflate(
                layout.item_bottom_loading,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is FoodViewHolder) {
            holder.bindData(dataList[position])
            setAnimation(holder.itemView, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position].id.isEmpty()) VIEW_TYPE_LOADING
        else VIEW_TYPE_ITEM
    }

    fun setFoods(foods: MutableList<Food>) {
        updateData(foods, DiffUtil.calculateDiff(FoodsDiffCallBack(getData(), foods)))
    }

    fun addLoadingItem() {
        if (dataList[dataList.size - 1].id.isNotEmpty()) {
            dataList.add(Food())
            notifyItemRangeInserted(dataList.size - 1, 1)
        }
    }

    fun removeLoadingItem() {
        if (dataList.size > Constant.DEFAULT_VALUE && dataList[dataList.size - 1].id.isEmpty()) {
            dataList.removeAt(dataList.size - 1)
            notifyItemRangeRemoved(dataList.size - 1, 1)
        }
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.from_right)
            view.startAnimation(animation)
            lastPosition = position
        }
    }

    fun setLastPositionItem(position: Int = Constant.INVALID_VALUE) {
        lastPosition = position
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        if (holder is FoodViewHolder) holder.clearAnimation()
    }

    companion object {

        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1

        class FoodViewHolder(view: View, private val listener: OnItemClickListener<Food>?) :
            RecyclerView.ViewHolder(view) {

            fun bindData(food: Food) {
                itemView.run {
                    itemView.textViewFoodName.text = food.name
                    itemView.textViewCost.text = context.getString(
                        R.string.text_display_food_cost,
                        food.cost,
                        if (food.unitCost!!.isNotEmpty()) food.unitCost else context.getString(R.string.text_vn_money),
                        food.unitFood
                    )
                    itemView.textViewAmountBuy.text =
                        context.getString(R.string.text_display_amount_buy, food.amountBuy)
                    itemView.textViewOutOfFood.setState(!food.state)
                    itemView.imageViewNew.setState(food.dateCreated.validateItemDuration(Constant.RULE_NEW_FOOD_FOLLOW_DAY))
                    itemView.imageViewFood.loadImageUrl(food.imgUrl.replaceIpAddress())
                    setOnClickListener { listener?.onItemViewClick(food, adapterPosition) }
                }
            }

            fun clearAnimation() {
                itemView.clearAnimation()
            }
        }

        class ItemLoading(view: View) : ViewHolder(view)
    }
}