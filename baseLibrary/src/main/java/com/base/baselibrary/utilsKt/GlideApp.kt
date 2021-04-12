package com.base.baselibrary.utilsKt

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

@JvmOverloads
fun glideShow(
    context: Context?,
    string: String?,
    imageView: ImageView?,
    @DrawableRes placeholder: Int = -1
) {
    if (context == null) {
        return
    }

    if (imageView == null) {
        return
    }

    if (context is Activity && context.isDestroyed) {
        return
    }

    if (placeholder == -1) {
        Glide.with(context).load(string).into(imageView)
    } else {
        Glide.with(context).load(string).placeholder(placeholder)
            .into(imageView)
    }
}

@JvmOverloads
fun glideShow(
    context: Context?,
    @DrawableRes drawableResId: Int,
    imageView: ImageView?,
    @DrawableRes placeholder: Int = -1
) {
    if (context == null) {
        return
    }

    if (imageView == null) {
        return
    }

    if (context is Activity && context.isDestroyed) {
        return
    }

    if (placeholder == -1) {
        Glide.with(context).load(drawableResId).into(imageView)
    } else {
        Glide.with(context).load(drawableResId).placeholder(placeholder).into(imageView)
    }
}


