package com.module.base.utils

import com.amap.api.location.AMapLocation

/**
Create by Darren
On 2021/1/27 19:39

 **/

fun AMapLocation?.isSuccess () : Boolean {
    return this != null && this.errorCode ==AMapLocation.LOCATION_SUCCESS && !this.city.isNullOrEmpty()
}