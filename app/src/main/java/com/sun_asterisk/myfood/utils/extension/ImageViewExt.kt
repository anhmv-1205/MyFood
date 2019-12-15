package com.sun_asterisk.myfood.utils.extension

import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.annotation.OrderStatus
import java.io.File

fun ImageView.loadImageUrl(url: String?, imageError: Int? = null) {
    if (imageError != null)
        Glide.with(context).load(url).thumbnail(Constant.THUMBNAIL_MULTIPLIER).error(imageError).into(this)
    else Glide.with(context).load(url).thumbnail(Constant.THUMBNAIL_MULTIPLIER)
        .centerCrop()
        .transition(GenericTransitionOptions.with(R.anim.fade_in))
        .into(this)
}

fun ImageView.setIconByStatusStatus(status: String) {
    this.setBackgroundResource(
        when (status) {
            OrderStatus.REQUESTING -> R.drawable.ic_request
            OrderStatus.REJECTED -> R.drawable.ic_reject
            OrderStatus.APPROVED -> R.drawable.ic_approve
            OrderStatus.DONE -> R.drawable.ic_done
            else -> R.drawable.ic_cancel
        }
    )
}

fun ImageView.loadImagePath(path: String) {
    Glide.with(context)
        .load(File(path))
        .into(this)
}
