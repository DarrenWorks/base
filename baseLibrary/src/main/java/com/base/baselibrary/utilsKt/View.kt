package com.base.baselibrary.utilsKt

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
fun ImageView.glideShow(path: String) {
    glideShow(context, path, this)
}

fun TextView.checkEmpty(hint: String): Boolean {
    return if (TextUtils.isEmpty(text)) {
        showToastShort(hint)
        false
    } else {
        true
    }
}

@JvmOverloads
fun EditText.checkEmpty(hint: String = getHint().toString()): Boolean {
    return if (TextUtils.isEmpty(text)) {
        showToastShort(hint)
        false
    } else {
        true
    }
}

fun EditText.addTextChangedListener(
    beforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
    onTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
    afterTextChanged: (s: Editable?) -> Unit = { _ -> }
) {
    addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
           afterTextChanged(s)
        }
    })
}