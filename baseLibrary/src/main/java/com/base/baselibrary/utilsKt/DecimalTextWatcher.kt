package com.base.baselibrary.utilsKt

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
Create by Darren
On 2020/5/25 16:07
保留n位小数
 **/
open class DecimalTextWatcher(private val editText : EditText, private val decimal : Int) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val text = s ?: ""
        if (text.contains(".")) {
            val index = text.indexOf(".")
            if (text.length - index > decimal + 1) {
               editText.setText(text.substring(0, index + decimal + 1))
                editText.setSelection(editText.text.length)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}