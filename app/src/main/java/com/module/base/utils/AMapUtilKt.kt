package com.module.base.utils

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.base.baselibrary.utilsKt.showToastShort
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
Create by Darren
On 2021/2/18 10:56
高德定位工具类——kotlin
 **/
class AMapUtilKt {
    companion object {
        private fun initLocation(context: Context, once: Boolean, listener: AMapLocationListener) {
            //初始化定位
            val mLocationClient: AMapLocationClient = AMapLocationClient(context)
            //设置定位回调监听
            mLocationClient.setLocationListener(listener)
            //初始化AMapLocationClientOption对象
            val mLocationOption: AMapLocationClientOption = AMapLocationClientOption()

            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.isOnceLocation = once

            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.isNeedAddress = true
            //定位回调间隔
            mLocationOption.interval = 30000L
            //关闭缓存机制
            mLocationOption.isLocationCacheEnable = true
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption)
            //启动定位
            mLocationClient.startLocation()
        }

        suspend fun locationOnce(context: Context): AMapLocation? {
            return suspendCoroutine { cont ->
                PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.LOCATION)
                    .callback(object : PermissionUtils.SimpleCallback{
                        override fun onGranted() {
                            initLocation(context, true) { location ->
                                if (location.isSuccess()) {
                                    cont.resume(location)
                                } else{
                                    cont.resume(null)
                                    showToastShort("定位失败")
                                }

                            }
                        }

                        override fun onDenied() {
                            showToastShort("无相关权限，无法进行定位")
                            cont.resume(null)
                        }
                    })
                    .request()
            }
        }
    }
}
