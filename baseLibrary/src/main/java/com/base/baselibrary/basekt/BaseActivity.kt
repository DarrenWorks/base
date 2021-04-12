package com.base.baselibrary.basekt

import activitystarter.ActivityStarter
import android.R
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.base.baselibrary.base.Orientation
import com.base.baselibrary.base.StatusBarMode
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.Utils
import com.gyf.immersionbar.ImmersionBar
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

/**
 * Create by Darren
 * On 2020/10/9 10:47
 * activity基类
 */
open class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    protected val activity: BaseActivity<T>
        get() = this
    protected val context: Context
        get() = this
    protected val appContext: Context
        get() = Utils.getApp()
    protected lateinit var binding: T
    protected var orientation = Orientation.PORTRAIT
    protected var statusBarMode = StatusBarMode.IMMEPIC

    @ColorRes
    protected var statusBarColor = R.color.white
    protected var statusBarDarkFont = true
    protected var fixKeyboard = true
    private val compositeDisposable = CompositeDisposable()
    private var onReturn: (Intent?) -> Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityStarter.fill(this, savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<*>
        val inflate = clazz.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        binding = inflate.invoke(null, layoutInflater, null, false) as T
        setContentView(binding.root)
        when (orientation) {
            Orientation.LANDSCAPE -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            Orientation.PORTRAIT -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            Orientation.FREE -> {
            }
        }
        initDefault()

        //状态栏沉浸式模式初始化
        initImmersionBar(
            statusBarMode,
            statusBarColor,
            statusBarDarkFont
        )

        //修复软键盘遮挡editText问题
        if (fixKeyboard) {
            KeyboardUtils.fixAndroidBug5497(window)
        }

        //修复某些机型会自动弹出软键盘的问题
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        require(initExtra(intent)) { "Can't get the expect data from intent." }
        init()
        initClicks()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ActivityStarter.save(this, outState)
    }

    /**
     * 配置默认属性
     * 建议修改下列属性时修改此方法
     *
     * @see .mStatusBarMode
     *
     * @see .mStatusBarColor
     *
     * @see .mStatusBarDarkFont
     *
     * @see .mFixKeyboard
     */
    protected open fun initDefault() {}

    /**
     * 处理extra获取，做一些保证开发期安全的判断
     *
     * @param intent
     * @return 如果数据不符合预期，返回false，将报出runtime错误
     */
    protected open fun initExtra(intent: Intent?): Boolean {
        return true
    }

    /**
     * 初始化工作
     */
    protected open fun init() {}

    /**
     * 初始化点击事件
     */
    protected open fun initClicks() {}

    /**
     * 为控件上方增加与状态栏等高的padding，一般用于处理沉浸式造成的状态栏与页面重叠
     *
     * @param views 要增加顶部padding的view
     */
    protected fun setTopByPadding(vararg views: View) {
        for (view in views) {
            if (view.isPaddingRelative) {
                view.setPaddingRelative(
                    view.paddingStart,
                    view.paddingTop + ImmersionBar.getStatusBarHeight(activity),
                    view.paddingEnd,
                    view.paddingBottom
                )
            } else {
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop + ImmersionBar.getStatusBarHeight(activity),
                    view.paddingRight,
                    view.paddingBottom
                )
            }
        }
    }

    /**
     * 为控件上方增加与状态栏等高的margin，一般用于处理沉浸式造成的状态栏与页面重叠
     *
     * @param views 要增加顶部margin的view
     */
    protected fun setTopByMargin(vararg views: View) {
        for (view in views) {
            val layoutParams = view.layoutParams as MarginLayoutParams
            layoutParams.topMargin += ImmersionBar.getStatusBarHeight(activity)
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
    protected fun antiShakeClick(
        view: View,
        onClick: () -> Unit,
        windowDuration: Long = 1,
        timeUnit: TimeUnit? = TimeUnit.SECONDS
    ) {
        val disposable = view.clicks().throttleLatest(windowDuration, timeUnit)
            .subscribe { runOnUiThread { onClick() } }
        addDisposable(disposable)
    }

    private fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun clearDisposable() {
        compositeDisposable.clear()
    }

    /**
     * 注册onActivityResult处理
     * 如在子类中重写
     *
     * @param intent
     * @param requestCode
     * @param onReturn    处理返回数据业务逻辑
     * @see .onActivityResult
     */
    protected fun startActivityForResult(
        intent: Intent?,
        requestCode: Int,
        onReturn: (Intent?) -> Unit
    ) {
        this.onReturn = onReturn
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            onReturn(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
    }
}