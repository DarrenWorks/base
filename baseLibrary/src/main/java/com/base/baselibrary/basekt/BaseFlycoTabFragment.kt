package com.base.baselibrary.basekt

import androidx.viewbinding.ViewBinding

/**
 * Create by Darren
 * On 2020/10/10 11:43
 * for smart tab layout
 */
open class BaseFlycoTabFragment<T : ViewBinding> : BaseFragment<T>() {
    private var mReadyToGetData = false
    override fun onResume() {
        super.onResume()
        if (!mReadyToGetData) {
            mReadyToGetData = true
        }
        if (!isHidden) {
            getData()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && mReadyToGetData) {
            getData()
        }
    }

    /**
     * 重写此方法以实现切换到此页时更新数据
     */
    protected open fun getData(): Unit = Unit
}