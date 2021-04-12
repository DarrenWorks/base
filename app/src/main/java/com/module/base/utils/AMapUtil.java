package com.module.base.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.base.baselibrary.utilsKt.GeneralUtilsKt;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;

public class AMapUtil {
    private static void initLocation(Context context, AMapLocationListener listener, boolean once) {
        AMapLocationClient mLocationClient;
        AMapLocationClientOption mLocationOption;
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(listener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(once);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //定位回调间隔
        mLocationOption.setInterval(30000L);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public static void startLocation(Context context, AMapLocationListener listener) {
        PermissionUtils.permission(PermissionConstants.LOCATION, PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        initLocation(context, listener, false);
                    }

                    @Override
                    public void onDenied() {
                        GeneralUtilsKt.showToastShort("无相关权限，无法进行定位");
                    }
                })
                .request();
    }

    public static void startLocationOnce(Context context, AMapLocationListener listener) {
        PermissionUtils.permission(PermissionConstants.LOCATION, PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        initLocation(context, listener, true);
                    }

                    @Override
                    public void onDenied() {
                        GeneralUtilsKt.showToastShort("无相关权限，无法进行定位");
                    }
                })
                .request();
    }
}
