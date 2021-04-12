package com.base.baselibrary.okgo

import android.widget.TextView
import com.lzy.okgo.request.PostRequest

fun <T : Any?> PostRequest<T>.params(key: String, value: TextView): PostRequest<T> = params(key, value.text.toString())
