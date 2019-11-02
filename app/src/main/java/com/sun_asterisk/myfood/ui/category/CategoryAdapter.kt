package com.sun_asterisk.myfood.ui.category

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.recyclerview.BaseRecyclerViewAdapter
import com.sun_asterisk.myfood.base.recyclerview.OnItemClickListener
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.utils.extension.loadImageUrl
import com.sun_asterisk.myfood.utils.extension.notNull
import com.sun_asterisk.myfood.utils.extension.replaceIpAddress
import kotlinx.android.synthetic.main.item_category.view.buttonDetail
import kotlinx.android.synthetic.main.item_category.view.imageViewCategory
import kotlinx.android.synthetic.main.item_category.view.textViewDescription
import kotlinx.android.synthetic.main.item_category.view.textViewFoodName

class CategoryAdapter(context: Context, dataList: MutableList<Category>) :
    BaseRecyclerViewAdapter<Category, ViewHolder>(context, dataList = dataList) {

    private var mListener: OnItemClickListener<Category>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ItemHolder(
            layoutInflater.inflate(R.layout.item_category, parent, false), mListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemHolder) holder.bindData(getItem(position))
    }

    fun setCategories(categories: MutableList<Category>) {
        setData(categories)
    }

    fun setOnItemCategoryListener(listener: OnItemClickListener<Category>?) {
        this.mListener = listener
    }

    companion object {

        class ItemHolder(itemView: View, listener: OnItemClickListener<Category>?) :
            RecyclerView.ViewHolder(itemView) {

            private var mCategory: Category? = null

            init {
                listener.notNull {
                    itemView.buttonDetail.setOnClickListener {
                        mCategory?.let {
                            listener?.onItemViewClick(it, adapterPosition)
                        }
                    }
                }
            }

            fun bindData(item: Category?) {
                item?.let {
                    itemView.textViewFoodName.text = item.name
                    itemView.textViewDescription.text = item.description
                    itemView.imageViewCategory.loadImageUrl(
                        item.imageUrl.replaceIpAddress(),
                        R.drawable.space
                    )
                }
            }
        }
    }
}
