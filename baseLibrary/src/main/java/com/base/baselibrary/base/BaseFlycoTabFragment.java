package com.base.baselibrary.base;

import androidx.viewbinding.ViewBinding;

/**
 * Create by Darren
 * On 2020/10/10 11:43
 * for smart tab layout
 **/
public class BaseFlycoTabFragment<T extends ViewBinding> extends BaseFragment<T> {
    private boolean mReadyToGetData = false;

    @Override
    public void onResume() {
        super.onResume();

        if (!mReadyToGetData) {
            mReadyToGetData = true;
        }

        if (!isHidden()) {
            getData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden && mReadyToGetData) {
            getData();
        }
    }

    /**
     * 重写此方法以实现切换到此页时更新数据
     */
    protected void getData() {

    }
}
