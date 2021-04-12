package com.base.baselibrary.utils

import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

/**
Create by Darren
On 2020/5/11 17:45
View 扩展函数
 **/
/**
 * @param path local path or net path
 */
fun ImageView.glideShow(path : String) {
    glideShow(context, path, this)
}

fun TextView.checkEmpty(hint : String) : Boolean {
    return if (TextUtils.isEmpty(text)) {
        showToastShort(hint)
        false
    } else {
        true
    }
}

@JvmOverloads
fun EditText.checkEmpty(hint : String = getHint().toString()) : Boolean {
    return if (TextUtils.isEmpty(text)) {
        showToastShort(hint)
        false
    } else {
        true
    }
}