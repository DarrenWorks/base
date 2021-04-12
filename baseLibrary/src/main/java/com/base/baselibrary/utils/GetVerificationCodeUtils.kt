package com.base.baselibrary.utils

import android.app.Activity
import android.os.CountDownTimer
import android.text.TextUtils
import android.widget.TextView
import com.base.baselibrary.okgo.JsonCallback
import com.base.baselibrary.okgo.LzyResponse
import com.blankj.utilcode.util.KeyboardUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response

private const val future = (1 * 60 * 1000).toLong()
private const val interval: Long = 1000

@JvmOverloads
fun getVerificationCode(url: String, getCode: TextView, phone: TextView, activity: Activity?, type: Int? = null) {
    activity?.let { activityNotNull ->
        KeyboardUtils.hideSoftInput(activityNotNull)
    }
    if (checkTVNotNull(phone, "请输入手机号") && isPhoneNumber(phone, "请输入正确的手机号")) {
        OkGo.post<LzyResponse<Any>>(url)

                .params("mobile", phone.text.toString())
                .apply {
                    type?.let {
                        params("type", type)
                    }
                }
                .execute(object : JsonCallback<LzyResponse<Any>>() {
                    override fun onSuccess(response: Response<LzyResponse<Any>>) {
                        if (response.body().code == 108) {
                            showToastShort(response.body().msg)
                            return
                        }
                        showToastShort("获取验证码成功")
                        getCode.isEnabled = false
                        val timer = object : CountDownTimer(
                                future,
                                interval
                        ) {
                            override fun onTick(millisUntilFinished: Long) {
                                if (getCode != null) {
                                    getCode.text = (millisUntilFinished / 1000).toString() + "秒后重试"
                                }
                            }

                            override fun onFinish() {
                                if (getCode != null) {
                                    getCode.text = "获取验证码"
                                    getCode.isEnabled = true
                                }
                            }
                        }
                                .start()
                    }
                })
    }
}

fun getVerificationCode(url: String, getCode: TextView, phone: String, activity: Activity?, type: Int? = null) {
    activity?.let { activityNotNull ->
        KeyboardUtils.hideSoftInput(activityNotNull)
    }
    if (TextUtils.isEmpty(phone)) {
        showToastShort("请输入手机号")
    } else if (isPhoneNumber(phone, "请输入正确的手机号")) {
        OkGo.post<LzyResponse<Any>>(url)
                .params("mobile", phone)
                .apply {
                    type?.let {
                        params("type", type)
                    }
                }
                .execute(object : JsonCallback<LzyResponse<Any>>() {
                    override fun onSuccess(response: Response<LzyResponse<Any>>) {
                        getCode.isEnabled = false
                        val timer = object : CountDownTimer(
                                future,
                                interval
                        ) {
                            override fun onTick(millisUntilFinished: Long) {
                                if (getCode != null) {
                                    getCode.text = (millisUntilFinished / 1000).toString() + "秒后重试"
                                }
                            }

                            override fun onFinish() {
                                if (getCode != null) {
                                    getCode.text = "获取验证码"
                                    getCode.isEnabled = true
                                }
                            }
                        }
                                .start()
                    }
                })
    }
}