package com.module.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.base.baselibrary.base.BaseActivity;
import com.base.baselibrary.base.StatusBarMode;
import com.base.baselibrary.okgo.LoadingDialog;
import com.module.base.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    protected void init() {
        super.init();
        new LoadingDialog(mContext).show();
    }
}