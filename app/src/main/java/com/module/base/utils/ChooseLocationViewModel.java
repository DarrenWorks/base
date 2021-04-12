package com.module.base.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amap.api.services.core.LatLonPoint;

/**
 * Create by Darren
 * On 2020/10/28 11:58
 **/
public class ChooseLocationViewModel extends ViewModel {
        private final MutableLiveData<PoiSearchBean> mPoiSearchBean = new MutableLiveData<>();

    public LiveData<PoiSearchBean> getPoiSearchBean() {
        return mPoiSearchBean;
    }

    public void setPoiSearchBean(PoiSearchBean poiSearchBean) {
        mPoiSearchBean.postValue(poiSearchBean);
    }

    public static class PoiSearchBean {
            LatLonPoint mLatLonPoint;
            String city;

            public PoiSearchBean(LatLonPoint latLonPoint, String city) {
                mLatLonPoint = latLonPoint;
                this.city = city;
            }

            public LatLonPoint getLatLonPoint() {
                return mLatLonPoint;
            }

            public String getCity() {
                return city;
            }
        }
}
