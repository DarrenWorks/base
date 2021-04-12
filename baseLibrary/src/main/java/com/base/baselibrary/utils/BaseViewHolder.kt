package com.base.baselibrary.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
Create by Darren
On 2020/5/13 16:15
BRVAH
 **/
fun BaseViewHolder.glideShow(
    context: Context,
    path: String?,
    @IdRes imageViewId: Int,
    @DrawableRes placeholder: Int = -1
) : BaseViewHolder {
    com.base.baselibrary.utils.glideShow(context, path, getView(imageViewId), placeholder)
    return this
}

fun BaseViewHolder.glideShow(
    context: Context,
    @DrawableRes drawableResId: Int,
    @IdRes imageViewId: Int,
    @DrawableRes placeholder: Int = -1
) : BaseViewHolder {
    com.base.baselibrary.utils.glideShow(context, drawableResId, getView(imageViewId), placeholder)
    return this
}