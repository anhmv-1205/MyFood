package com.sun_asterisk.myfood.utils.extension

import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.utils.Constant

fun ImageView.loadImageUrl(url: String?, imageError: Int? = null) {
    if (imageError != null)
        Glide.with(context).load(url).thumbnail(Constant.THUMBNAIL_MULTIPLIER).error(imageError).into(this)
    else Glide.with(context).load(url).thumbnail(Constant.THUMBNAIL_MULTIPLIER)
        .centerCrop()
        .transition(GenericTransitionOptions.with(R.anim.fade_in))
        .into(this)
}
