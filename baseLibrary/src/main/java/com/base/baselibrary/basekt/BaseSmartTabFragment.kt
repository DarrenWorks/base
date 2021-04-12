package com.base.baselibrary.basekt

import androidx.viewbinding.ViewBinding

/**
 * Create by Darren
 * On 2020/10/10 11:39
 * for flyco.tablayout
 */
open class BaseSmartTabFragment<T : ViewBinding> : BaseFragment<T>() {
    private var mReadyToGetData = false
    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            data
        } else {
            mReadyToGetData = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && mReadyToGetData) {
            data
        }
    }

    /**
     * 重写此方法以实现切换到此页时更新数据
     */
    protected val data: Unit
        protected get() {}
}