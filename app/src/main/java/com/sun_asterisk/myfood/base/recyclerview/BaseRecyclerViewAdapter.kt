package com.sun_asterisk.myfood.base.recyclerview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun_asterisk.myfood.utils.extension.notNull

abstract class BaseRecyclerViewAdapter<T, V : RecyclerView.ViewHolder>
constructor(
    protected val context: Context,
    protected var layoutInflater: LayoutInflater = LayoutInflater.from(context),
    protected var dataList: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<V>() {

    protected var itemClickListener: OnItemClickListener<T>? = null

    protected var handler = Handler(Looper.getMainLooper())

    override fun getItemCount() = dataList.size

    fun getData(): MutableList<T> = dataList

    fun setData(data: MutableList<T>?) {
        data.notNull {
            dataList.clear()
            dataList.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun updateData(newData: MutableList<T>?, diffResult: DiffUtil.DiffResult) {
        handler.post {
            newData.notNull {
                dataList.clear()
                dataList.addAll(it)
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }

    fun clearData(isNotify: Boolean = true) {
        dataList.clear()
        if (isNotify) notifyDataSetChanged()
    }

    fun getItem(position: Int): T? = if (position < 0 || position >= dataList.size) null
    else dataList[position]

    fun addItem(data: T, position: Int) {
        dataList.add(position, data)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        if (position < 0 || position >= dataList.size) return
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun replaceItem(item: T, position: Int) {
        if (position < 0 || position >= dataList.size) return
        dataList[position] = item
        notifyItemInserted(position)
    }

    fun registerItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        itemClickListener = onItemClickListener
    }

    fun unRegisterItemClickListener() {
        itemClickListener = null
    }
}
