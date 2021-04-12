package com.base.baselibrary.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.viewbinding.ViewBinding;

import com.base.baselibrary.utils.Action;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.Utils;
import com.gyf.immersionbar.ImmersionBar;
import com.jakewharton.rxbinding3.view.RxView;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import activitystarter.ActivityStarter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Create by Darren
 * On 2020/10/9 10:47
 * activity基类
 **/
public class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected BaseActivity<T> mActivity;
    protected Context mContext;
    protected Context mAppContext;
    protected T mBinding;
    protected Orientation mOrientation = Orientation.PORTRAIT;
    protected StatusBarMode mStatusBarMode = StatusBarMode.IMMEPIC;
    protected @ColorRes
    int mStatusBarColor = android.R.color.white;
    protected boolean mStatusBarDarkFont = true;
    protected boolean mFixKeyboard = true;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Consumer<Intent> mOnReturn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        mContext = this;
        mAppContext = Utils.getApp();


        ActivityStarter.fill(this, savedInstanceState);

        try {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
            Method inflate = clazz.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            mBinding = (T) inflate.invoke(null, getLayoutInflater(), null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(mBinding.getRoot());

        switch (mOrientation) {
            case LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case FREE:
                //do nothing
                break;
        }

        initDefault();

        //状态栏沉浸式模式初始化
        ImmersionBarUtil.initImmersionBar(this, mStatusBarMode, mStatusBarColor, mStatusBarDarkFont, mFixKeyboard);

        //修复软键盘遮挡editText问题
        if (mFixKeyboard) {
            KeyboardUtils.fixAndroidBug5497(getWindow());
        }

        //修复某些机型会自动弹出软键盘的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (!initExtra(getIntent())) {
            throw new IllegalArgumentException("Can't get the expect data from intent.");
        }

        init();
        initClicks();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }

    /**
     * 配置默认属性
     * 建议修改下列属性时修改此方法
     *
     * @see #mStatusBarMode
     * @see #mStatusBarColor
     * @see #mStatusBarDarkFont
     * @see #mFixKeyboard
     */
    protected void initDefault() {

    }

    /**
     * 处理extra获取，做一些保证开发期安全的判断
     *
     * @param intent
     * @return 如果数据不符合预期，返回false，将报出runtime错误
     */
    protected boolean initExtra(Intent intent) {
        return true;
    }

    /**
     * 初始化工作
     */
    protected void init() {

    }

    /**
     * 初始化点击事件
     */
    protected void initClicks() {

    }

    /**
     * 为控件上方增加与状态栏等高的padding，一般用于处理沉浸式造成的状态栏与页面重叠
     *
     * @param views 要增加顶部padding的view
     */
    protected void setTopByPadding(View... views) {
        for (View view : views) {
            if (view.isPaddingRelative()) {
                view.setPaddingRelative(
                        view.getPaddingStart(),
                        view.getPaddingTop() + ImmersionBar.getStatusBarHeight(mActivity),
                        view.getPaddingEnd(),
                        view.getPaddingBottom()
                );
            } else {
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop() + ImmersionBar.getStatusBarHeight(mActivity),
                        view.getPaddingRight(),
                        view.getPaddingBottom()
                );
            }
        }
    }

    /**
     * 为控件上方增加与状态栏等高的margin，一般用于处理沉浸式造成的状态栏与页面重叠
     *
     * @param views 要增加顶部margin的view
     */
    protected void setTopByMargin(View... views) {
        for (View view : views) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.topMargin += ImmersionBar.getStatusBarHeight(mActivity);
        }
    }

    /**
     * 为控件增加防抖点击事件
     *
     * @param view           点击的控件
     * @param onClick        点击监听
     * @param windowDuration 防抖间隔
     * @param timeUnit       时间单位
     */
    protected void antiShakeClick(View view, Action onClick, long windowDuration, TimeUnit timeUnit) {
        Disposable disposable = RxView.clicks(view).throttleLatest(windowDuration, timeUnit).subscribe(unit -> runOnUiThread(onClick::run));
        addDisposable(disposable);
    }

    protected void antiShakeClick(View view, Action onClick, long windowDuration) {
        antiShakeClick(view, onClick, windowDuration, TimeUnit.SECONDS);
    }

    protected void antiShakeClick(View view, Action onClick) {
        antiShakeClick(view, onClick, 1);
    }

    private void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    private void clearDisposable() {
        mCompositeDisposable.clear();
    }

    /**
     * 注册onActivityResult处理
     * 如在子类中重写
     *
     * @param intent
     * @param requestCode
     * @param onReturn    处理返回数据业务逻辑
     * @see #onActivityResult(int, int, Intent)
     * 方法请调用super()
     */
    protected void startActivityForResult(Intent intent, int requestCode, Consumer<Intent> onReturn) {
        mOnReturn = onReturn;
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (mOnReturn != null) {
                mOnReturn.accept(data);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearDisposable();
    }
}


