package com.base.baselibrary.base;

import androidx.viewbinding.ViewBinding;

/**
 * Create by Darren
 * On 2020/10/10 11:39
 * for flyco.tablayout
 **/
public class BaseSmartTabFragment<T extends ViewBinding> extends BaseFragment<T> {
    private boolean mReadyToGetData = false;

    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            getData();
        } else {
            mReadyToGetData = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mReadyToGetData) {
            getData();
        }
    }

    /**
     * 重写此方法以实现切换到此页时更新数据
     */
    protected void getData() {

    }
}
