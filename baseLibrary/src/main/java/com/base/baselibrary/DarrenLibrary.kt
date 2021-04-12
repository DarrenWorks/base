package com.base.baselibrary

import android.app.Activity
import android.content.Context
import java.lang.reflect.Type

/**
Create by Darren
On 2020/5/11 11:54
DarrenLibrary
 **/
class DarrenLibrary {
    companion object {
        @JvmStatic
       lateinit var jumpToLoginCallback : (isJumpToLogin : Boolean) -> Unit
    }


    /**
     * You must call this in your Application class to init this library
     * @param jumpToLoginCallback What you will do after logout,
     * Regularly, you will jump to loginActivity or MainActivity according to the isJumpToLogin
     */
    fun initLogin(
        jumpToLoginCallback : (isJumpToLogin : Boolean) -> Unit
    ) {
       DarrenLibrary.jumpToLoginCallback = jumpToLoginCallback
    }



}