package com.module.base.utils

import com.flyco.tablayout.listener.CustomTabEntity

/**
Create by Darren
On 2021/1/13 16:26

 **/

class MyTabEntity(
    private val tabSelectedIcon: Int = 0, private val tabUnselectedIcon: Int = 0,
    private val tabTitle: String
) :
    CustomTabEntity {
    override fun getTabTitle(): String {
        return tabTitle
    }

    override fun getTabSelectedIcon(): Int {
        return tabSelectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return tabUnselectedIcon
    }

}