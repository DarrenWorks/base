package com.base.baselibrary.base;

import android.app.Activity;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

/**
 * Create by Darren
 * On 2020/10/9 12:18
 * 沉浸式工具类
 **/
public class ImmersionBarUtil {
    public static void initImmersionBar(Activity activity, StatusBarMode statusBarMode, @ColorRes int statusBarColor, boolean statusBarDarkFont, boolean fixKeyboard) {
        ImmersionBar immersionBar = ImmersionBar.with(activity);
        switch (statusBarMode) {
            case DEFAULT:
                immersionBar.fitsSystemWindows(true);
                immersionBar.statusBarColor(statusBarColor);
                break;
            case IMMEPIC:
                immersionBar.transparentStatusBar();
                break;
        }
        immersionBar.statusBarDarkFont(statusBarDarkFont);
//        immersionBar.keyboardEnable(fixKeyboard);
        immersionBar.init();
    }

    public static void initImmersionBar(Fragment fragment, StatusBarMode statusBarMode, @ColorRes int statusBarColor, boolean statusBarDarkFont, boolean fixKeyboard) {
        ImmersionBar immersionBar = ImmersionBar.with(fragment);
        switch (statusBarMode) {
            case DEFAULT:
                immersionBar.fitsSystemWindows(true);
                immersionBar.statusBarColor(statusBarColor);
                break;
            case IMMEPIC:
                immersionBar.transparentStatusBar();
                break;
        }
        immersionBar.statusBarDarkFont(statusBarDarkFont);
//        immersionBar.keyboardEnable(fixKeyboard);
        immersionBar.init();
    }
}
