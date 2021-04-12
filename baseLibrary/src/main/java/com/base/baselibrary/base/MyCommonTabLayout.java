package com.base.baselibrary.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.FragmentUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.utils.FragmentChangeManager;

import java.util.ArrayList;

/**
 * Create by Darren
 * On 2020/12/22 18:03
 * 解决此库在fragment中使用时不能正确传入FragmentManager的问题
 **/
public class MyCommonTabLayout extends CommonTabLayout {
    private FragmentChangeManager mFragmentChangeManager;

    public MyCommonTabLayout(Context context) {
        super(context);
    }

    public MyCommonTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTabData(ArrayList<CustomTabEntity> tabEntitys, FragmentManager fragmentManager, int containerViewId, ArrayList<Fragment> fragments) {
        FragmentUtils.removeAll(fragmentManager);
        mFragmentChangeManager = new FragmentChangeManager(fragmentManager, containerViewId, fragments);
        setTabData(tabEntitys);
    }

    @Override
    public void setCurrentTab(int currentTab) {
        super.setCurrentTab(currentTab);
        if (mFragmentChangeManager != null) {
            mFragmentChangeManager.setFragments(currentTab);
        }
    }

    public FragmentChangeManager getFragmentChangeManager() {
        return mFragmentChangeManager;
    }
}
