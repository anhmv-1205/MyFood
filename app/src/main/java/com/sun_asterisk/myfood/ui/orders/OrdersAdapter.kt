package com.sun_asterisk.myfood.ui.orders

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.recyclerview.BaseRecyclerViewAdapter
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import com.sun_asterisk.myfood.utils.extension.setStatusOfOrder
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
import kotlinx.android.synthetic.main.item_order.view.imageViewFood
import kotlinx.android.synthetic.main.item_order.view.textViewFoodName
import kotlinx.android.synthetic.main.item_order.view.textViewStatus
import kotlinx.android.synthetic.main.item_order.view.textViewTimeBuy
import java.util.Locale

class OrdersAdapter(context: Context, dataList: MutableList<Order>) :
    BaseRecyclerViewAdapter<Order, ViewHolder>(context, dataList = dataList) {

    private var lastPosition = Constant.INVALID_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM)
            OrderViewHolder(layoutInflater.inflate(R.layout.item_order, parent, false), itemClickListener)
        else ItemLoading(layoutInflater.inflate(R.layout.item_bottom_loading, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is OrderViewHolder) {
            holder.bindData(dataList[position])
            setAnimation(holder.itemView, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position].id.isEmpty()) VIEW_TYPE_LOADING
        else VIEW_TYPE_ITEM
    }

    fun removeLoadingItem() {
        if (dataList.size > Constant.DEFAULT_VALUE && dataList[dataList.size - 1].id.isEmpty()) {
            dataList.removeAt(dataList.size - 1)
            notifyItemRangeRemoved(dataList.size - 1, 1)
        }
    }

    fun setOrders(orders: MutableList<Order>) {
        updateData(orders, DiffUtil.calculateDiff(OrdersDiffUtill(getData(), orders)))
    }

    fun updateOrder(order: Order, position: Int) {
        if (position < 0 || position >= dataList.size) return
        dataList[position].status = order.status
        notifyItemRangeChanged(position, 1)
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
        if (holder is OrderViewHolder) holder.clearAnimation()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1

        class OrderViewHolder(view: View, private val listener: OnItemClickListener<Order>?) :
            RecyclerView.ViewHolder(view) {

            fun bindData(order: Order) {
                itemView.run {
                    itemView.textViewFoodName.text = order.food.name
                    itemView.textViewTimeBuy.text = context.getString(
                        R.string.text_time_buy_and_shift, order.date_buy.toDateWithFormat(
                            Constant.DATETIME_FORMAT_YYYY_MM_DD,
                            context.getString(R.string.text_time_format)
                        ), order.shift.toUpperCase(Locale.US)
                    )
                    itemView.textViewStatus.setStatusOfOrder(order.status)
                    itemView.imageViewFood.loadImageUrl(order.food.imgUrl.replaceIpAddress())
                    setOnClickListener { listener?.onItemViewClick(order, adapterPosition) }
                }
            }

            fun clearAnimation() {
                itemView.clearAnimation()
            }
        }

        class ItemLoading(view: View) : ViewHolder(view)
    }
}
