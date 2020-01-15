package com.sun_asterisk.myfood.ui.comment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.recyclerview.BaseRecyclerViewAdapter
import com.sun_asterisk.myfood.data.model.Comment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.toDateWithFormat
import kotlinx.android.synthetic.main.item_comment.view.ratingBar
import kotlinx.android.synthetic.main.item_comment.view.textViewCommenter
import kotlinx.android.synthetic.main.item_comment.view.textViewContentComment
import kotlinx.android.synthetic.main.item_comment.view.textViewRating
import kotlinx.android.synthetic.main.item_comment.view.textViewTimeComment

class CommentAdapter(context: Context, list: ArrayList<Comment>) :
    BaseRecyclerViewAdapter<Comment, ViewHolder>(context, dataList = list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ItemHolder(layoutInflater.inflate(R.layout.item_comment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.bindData(dataList[position])
        }
    }

    companion object {
        class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindData(comment: Comment) {
                itemView.run {
                    textViewCommenter.text = comment.buyer.name
                    textViewTimeComment.text = comment.dateCreated.toDateWithFormat(
                        Constant.DATETIME_FORMAT_FULL, context.getString(
                            R.string.text_time_format_hh_mm_dd_mm_yyyy
                        )
                    )
                    textViewRating.text = comment.rating.toString()
                    ratingBar.rating = comment.rating.toFloat()
                    textViewContentComment.text = comment.comment
                }
            }
        }
    }
}
