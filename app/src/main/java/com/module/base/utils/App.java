package com.module.base.utils;

import android.app.Activity;
import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.base.baselibrary.DarrenLibrary;
import com.base.baselibrary.utils.DynamicTimeFormat;
import com.base.baselibrary.utils.User;
import com.blankj.utilcode.util.ActivityUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import kotlin.Unit;
import okhttp3.OkHttpClient;


public class App extends Application {
    public static App instance;

    static {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            //全局设置（优先级最低）
//                layout.setEnableNestedScroll(false);
            layout.setEnableLoadMore(true);//是否开启底部刷新
            layout.setEnableAutoLoadMore(false);//设置是否监听列表在滚动到底部时触发加载事件（默认true）
            layout.setEnableOverScrollDrag(false);//设置是否启用越界拖动（仿苹果效果）
            layout.setEnableOverScrollBounce(true);//设置是否启用越界回弹
            layout.setEnableLoadMoreWhenContentNotFull(true);//设置在内容不满一页的时候，是否可以上拉加载更多
            layout.setEnableScrollContentWhenRefreshed(false);//是否在刷新完成之后滚动内容显示新数据
            layout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
            layout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
            layout.setPrimaryColorsId(android.R.color.white, android.R.color.black);
            return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));
    }

    public String mLocationId;
    private String mLocationName;
    private boolean locationProcessing = false;
    private List<OnLocateDoneListener> mOnLocateDoneListeners = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initOkGo();
        initDarrenLib();
    }

    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        interceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        interceptor.setColorLevel(Level.INFO);
        //公共参数，token
        builder.addInterceptor(interceptor);
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .addCommonParams(new HttpParams(User.Key.TOKEN.getKey(), User.getString(User.Key.TOKEN)));

    }

    private void initDarrenLib() {
        DarrenLibrary.setJumpToLoginCallback(isLogin -> {
            if (isLogin) {
                Activity top = ActivityUtils.getTopActivity();
               /* if (!(top instanceof LoginActivity)) {
                    LoginActivityStarter.start(top);
                    if (!(top instanceof MainActivity)) {
                        top.finish();
                    } else {
                        top.runOnUiThread(() -> ((MainActivity) top).setCurrentPage(0));
                    }
                }*/
            } else {
//                MainActivityStarter.start(ActivityUtils.getTopActivity());
            }
            return Unit.INSTANCE;
        });
    }


    public interface OnLocateDoneListener {
        void onLocationDone(String name);
    }
}
