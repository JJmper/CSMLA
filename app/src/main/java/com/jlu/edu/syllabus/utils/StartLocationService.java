package com.jlu.edu.syllabus.utils;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import utils.MyApplication;

/**
 * Created by zhengheming on 2016/3/11.
 */
public class StartLocationService {

    private static final StartLocationService StartLocationService = null;
    private LocationClient mLocClient;

    private StartLocationService() {

    }

    private StartLocationService(Application application) {
        locate(application);
    }

    public static synchronized StartLocationService GetInstent(Application application, StartLocationService startLocationService) {
        if (startLocationService == null) {
            return new StartLocationService(application);
        } else {
            return startLocationService;
        }
    }

    private void locate(Application application) {
        // 定位服务
        mLocClient = ((MyApplication) application).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        // option.setCoorType(mCoorEdit.getText().toString()); // 设置坐标类型
        option.setServiceName("com.baidu.location.service_v2.9");
        option.setPoiExtraInfo(true);
        option.setAddrType("all");
        option.setScanSpan(300); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位

        if (true) {
            option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
        } else {
            option.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先
        }
        option.setPoiNumber(10);
        option.disableCache(true);
        mLocClient.setLocOption(option);

    }

    public void StartLocation() {

        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.requestLocation();
        } else if ((mLocClient != null) && (!mLocClient.isStarted())) {
            mLocClient.start();//打开定位
        }
    }
}
