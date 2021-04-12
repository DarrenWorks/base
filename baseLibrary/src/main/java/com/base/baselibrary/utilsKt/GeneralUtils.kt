package com.base.baselibrary.utilsKt

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import java.io.FileNotFoundException
import java.util.regex.Pattern

/**
 * Check if the textView is not empty.
 * Show toast if it is empty.
 * @param toast the content of toast is showed.
 * @return false if textView is empty
 */
fun checkTVNotNull(text: TextView, toast: String): Boolean {
    return if (TextUtils.isEmpty(text.text.toString())) {
        ToastUtils.showShort(toast)
        false
    } else true
}

/**
 * toast from resource
 * @see checkTVNotNull
 */
fun checkTVNotNull(text: TextView, @StringRes toast: Int): Boolean {
    return checkTVNotNull(text, StringUtils.getString(toast))
}

/**
 * toast the hint of EditText
 * @see checkTVNotNull
 */
fun checkTVNotNull(text: EditText) : Boolean {
    return checkTVNotNull(text, text.hint.toString())
}

/**
 * check every EditText of texts
 * @see checkTVNotNull
 */
fun checkTVNotNull(vararg texts: EditText) : Boolean {
    for (text in texts) {
        if (!checkTVNotNull(text)) return false
    }
    return true
}

fun checkTVEquals(text1: TextView, text2: TextView, toast: String) : Boolean {
   return if (text1.text.toString() != text2.text.toString()) {
       showToastShort(toast)
         false
    } else true
}

fun getString(text: TextView): String {
    return text.text.toString()
}

fun showToastShort(toast: String) {
    Toast.makeText(Utils.getApp().applicationContext, toast, Toast.LENGTH_SHORT).show()
}

fun showToastLong(toast: String) {
    Toast.makeText(Utils.getApp().applicationContext, toast, Toast.LENGTH_LONG).show()
}

fun showToastShort(@StringRes toast: Int) {
    Toast.makeText(Utils.getApp().applicationContext, toast, Toast.LENGTH_SHORT).show()
}

fun showToastLong(@StringRes toast: Int) {
    Toast.makeText(Utils.getApp().applicationContext, toast, Toast.LENGTH_LONG).show()
}

fun isPhoneNumber(phoneNumber: String, toast: String = ""): Boolean {
    if (TextUtils.isEmpty(phoneNumber)) {
        return false
    }
    val matcher = Pattern.compile("^1(0|3|4|5|8|9|7)\\d{9}$").matcher(phoneNumber)
    var isPhoneNumber = matcher.matches()
    if (!TextUtils.isEmpty(toast) && !isPhoneNumber) {
        showToastShort(toast)
    }
    return isPhoneNumber
}

fun isPhoneNumber(phoneNumber: TextView, toast: String = ""): Boolean {
    if (TextUtils.isEmpty(phoneNumber.text.toString())) {
        return false
    }
    val matcher = Pattern.compile("^1(0|3|4|5|8|9|7)\\d{9}$").matcher(phoneNumber.text.toString())
    var isPhoneNumber = matcher.matches()
    if (!TextUtils.isEmpty(toast) && !isPhoneNumber) {
        showToastShort(toast)
    }
    return isPhoneNumber
}

fun resolveUrl(url: String?, urlBase: String) : String? {
    return when {
        url == null -> url
        url.startsWith("http") -> url
        else -> urlBase + url
    }
}

fun setMoneyTextWatcher(money: EditText, decimal: Int) {
    money.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            if (s.contains(".")) {
                val index = s.indexOf(".")
                if (s.length - index > decimal + 1) {
                    money.setText(s.substring(0, s.length - (s.length - index - decimal - 1)))
                    money.setSelection(s.length - 1)
                }
            }
        }
    })
}

/**
 * 阿拉伯数字转汉字
 */
fun int2chineseNum(src: Int): String {
    var srcLocal = src
    val num = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    val unit = arrayOf("", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千")
    var dst = ""
    var count = 0
    while (srcLocal > 0) {
        dst = num[srcLocal % 10] + unit[count] + dst
        srcLocal /= 10
        count++
    }
    return dst.replace("零[千百十]".toRegex(), "零").replace("零+万".toRegex(), "万")
        .replace("零+亿".toRegex(), "亿").replace("亿万".toRegex(), "亿零")
        .replace("零+".toRegex(), "零").replace("零$".toRegex(), "")
}

fun updateMedia(context: Context, path: String, name: String) {
    MediaScannerConnection.scanFile(context, arrayOf(path + name), null) { _, uri ->
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver, "$path$name", name, null)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = uri
        context.sendBroadcast(mediaScanIntent)
    }
}

/**
 * 解决edittext 和 scrollview 滚动冲突
 */
@SuppressLint("ClickableViewAccessibility")
fun resolveEditTextAndScrollViewConflict(editText: EditText) {
    val onTouchListener = View.OnTouchListener { view, event ->
        if (view.id == editText.id && canVerticalScroll(editText)) {
            view.parent.requestDisallowInterceptTouchEvent(true)
            if (event.action == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return@OnTouchListener false
    }

    editText.setOnTouchListener(onTouchListener)

}

fun canVerticalScroll(editText: EditText): Boolean {
    val scrollY = editText.scrollY
    //控件内容的总高度
    //控件内容的总高度
    val scrollRange = editText.layout.height
    //控件实际显示的高度
    //控件实际显示的高度
    val scrollExtent = editText.height - editText.compoundPaddingTop - editText.compoundPaddingBottom
    //控件内容总高度与实际显示高度的差值
    //控件内容总高度与实际显示高度的差值
    val scrollDifference = scrollRange - scrollExtent

    return if (scrollDifference == 0) {
        false
    } else scrollY > 0 || scrollY < scrollDifference - 1

}

