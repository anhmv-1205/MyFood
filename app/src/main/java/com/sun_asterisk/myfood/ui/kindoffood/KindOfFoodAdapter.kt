package com.sun_asterisk.myfood.ui.kindoffood

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.recyclerview.BaseRecyclerViewAdapter
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.KindOfFood
import com.sun_asterisk.myfood.utils.extension.notNull
import kotlinx.android.synthetic.main.item_kind_of_food.view.buttonDetail
import kotlinx.android.synthetic.main.item_kind_of_food.view.textViewDescription
import kotlinx.android.synthetic.main.item_kind_of_food.view.textViewFoodName

class KindOfFoodAdapter(context: Context, dataList: MutableList<KindOfFood>) :
    BaseRecyclerViewAdapter<KindOfFood, ViewHolder>(context, dataList = dataList) {

    private var listener: OnItemClickListener<KindOfFood>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ItemHolder(
            layoutInflater.inflate(R.layout.item_kind_of_food, parent, false), listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemHolder) holder.bindData(getItem(position))
    }

    fun setKindOfFoods(kindOfFoods: MutableList<KindOfFood>) {
        setData(kindOfFoods)
    }

    fun setOnItemKindOfFoodListener(listener: OnItemClickListener<KindOfFood>?) {
        this.listener = listener
    }

    companion object {

        class ItemHolder(itemView: View, listener: OnItemClickListener<KindOfFood>?) :
            RecyclerView.ViewHolder(itemView) {

            private var kindOfFood: KindOfFood? = null

            init {
                listener.notNull {
                    itemView.buttonDetail.setOnClickListener {
                        kindOfFood?.let {
                            listener?.onItemViewClick(it, adapterPosition)
                        }
                    }
                }
            }

            fun bindData(item: KindOfFood?) {
                item?.let {
                    itemView.textViewFoodName.text = item.title
                    itemView.textViewDescription.text = item.description
                }
            }
        }
    }
}
