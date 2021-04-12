package com.base.baselibrary.utils

import android.text.TextUtils
import com.base.baselibrary.DarrenLibrary.Companion.jumpToLoginCallback
import com.base.baselibrary.utilsKt.showToastShort
import com.blankj.utilcode.util.SPUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.HttpParams

object User {
    @JvmStatic
    private val user: SPUtils
        get() = SPUtils.getInstance("user")

    @JvmStatic
    fun put(key: Key, value: Int) {
        user.put(key.key, value, true)
    }

    @JvmStatic
    fun put(key: Key, value: Float) {
        user.put(key.key, value, true)
    }

    @JvmStatic
    fun put(key: Key, value: Long) {
        user.put(key.key, value, true)
    }

    @JvmStatic
    fun put(key: Key, value: Boolean) {
        user.put(key.key, value, true)
    }

    @JvmStatic
    fun put(key: Key, value: String?) {
        user.put(key.key, value, true)
    }

    @JvmStatic
    fun getInt(key: Key): Int {
        return user.getInt(key.key)
    }

    @JvmStatic
    fun getFloat(key: Key): Float {
        return user.getFloat(key.key)
    }

    @JvmStatic
    fun getLong(key: Key): Long {
        return user.getLong(key.key)
    }

    @JvmStatic
    fun getBoolean(key: Key): Boolean {
        return user.getBoolean(key.key)
    }

    @JvmStatic
    fun getString(key: Key): String {
        return user.getString(key.key)
    }

    @JvmStatic
    fun isLogin(): Boolean {
        val token = getString(Key.TOKEN)
        return !TextUtils.isEmpty(token)
    }

    /**
     * @param login 是否跳转登录页
     */

    @JvmStatic
    fun logOut(login: Boolean) {
        user.clear(true)
        OkGo.getInstance().commonParams?.clear()
        jumpToLoginCallback(login)
    }

    @JvmStatic
    fun login(userName: String) {
        user.put("userName", userName)
    }

    @JvmStatic
    fun saveUserInfo(token: String?) {
        put(Key.TOKEN, token)
        OkGo.getInstance().addCommonParams(HttpParams(Key.TOKEN.key, token))
        showToastShort("登录成功")
    }

    enum class Key(var key: String) {
        TOKEN("token"),
        Authorization("Authorization"),
    }
}

