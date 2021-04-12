package com.base.baselibrary.basekt

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.base.baselibrary.base.StatusBarMode
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar

/**
 * Create by Darren
 * On 2020/10/9 12:18
 * 沉浸式工具类
 */
fun Activity.initImmersionBar(
    statusBarMode: StatusBarMode?,
    @ColorRes statusBarColor: Int,
    statusBarDarkFont: Boolean
) {
    immersionBar {
        when (statusBarMode) {
            StatusBarMode.DEFAULT -> {
                fitsSystemWindows(true)
                statusBarColor(statusBarColor)
            }
            StatusBarMode.IMMEPIC -> {
                transparentStatusBar()
            }
        }
        statusBarDarkFont(statusBarDarkFont)
    }
}

fun Fragment.initImmersionBar(
    statusBarMode: StatusBarMode?,
    @ColorRes statusBarColor: Int,
    statusBarDarkFont: Boolean
) {
    immersionBar {
        when (statusBarMode) {
            StatusBarMode.DEFAULT -> {
                fitsSystemWindows(true)
                statusBarColor(statusBarColor)
            }
            StatusBarMode.IMMEPIC -> {
                transparentStatusBar()
            }
        }
        statusBarDarkFont(statusBarDarkFont)
    }
}
