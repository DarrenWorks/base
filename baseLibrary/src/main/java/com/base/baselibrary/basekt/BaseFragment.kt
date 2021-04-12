package com.base.baselibrary.basekt

import activitystarter.ActivityStarter
import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.base.baselibrary.base.StatusBarMode
import com.base.baselibrary.utils.Action
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
 * On 2020/10/10 11:20
 * bottomSheetDialogFragment基类
 */
open class BaseFragment<T : ViewBinding> : Fragment() {
    protected val fragment: BaseFragment<T>
        get() = this
    protected val appContext: Context
        get() = Utils.getApp()
    protected lateinit var binding: T
    protected var statusBarMode = StatusBarMode.IMMEPIC

    @ColorRes
    protected var statusBarColor = R.color.white
    protected var statusBarDarkFont = true
    protected var fixKeyboard = true
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityStarter.fill(this, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<*>
        val inflate = clazz.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )
        binding = inflate.invoke(null, layoutInflater, container, false) as T

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDefault()

        //状态栏沉浸式模式初始化
        initImmersionBar(
            statusBarMode,
            statusBarColor,
            statusBarDarkFont
        )

        //修复软键盘遮挡editText问题
        if (fixKeyboard) {
            if (activity != null) {
                KeyboardUtils.fixAndroidBug5497(activity!!)
            }
        }
        require(initExtra(arguments)) { "Can't get the expect data from intent." }
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
    protected open fun initExtra(intent: Bundle?): Boolean {
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
                    view.paddingTop + ImmersionBar.getStatusBarHeight(fragment!!),
                    view.paddingEnd,
                    view.paddingBottom
                )
            } else {
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop + ImmersionBar.getStatusBarHeight(fragment!!),
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
            layoutParams.topMargin += ImmersionBar.getStatusBarHeight(fragment)
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
        val disposable =
            view.clicks().throttleLatest(windowDuration, timeUnit).subscribe {
                activity?.runOnUiThread { onClick() }
            }
        addDisposable(disposable)
    }

    private fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun clearDisposable() {
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
    }
}