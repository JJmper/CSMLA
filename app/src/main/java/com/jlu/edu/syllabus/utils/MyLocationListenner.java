package com.jlu.edu.syllabus.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;


/**
 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
 */
public class MyLocationListenner implements BDLocationListener {
    private Context context;
    private LocationClient locationClient;
    public static double longitude=0;
    public static double latitude=0;

    public MyLocationListenner(Context context, LocationClient locationClient) {
        this.context = context;
        this.locationClient = locationClient;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null)
            return;

        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Log.i("LocationListener", longitude+"--"+latitude);

    }

    public void onReceivePoi(BDLocation poiLocation) {
        if (poiLocation == null) {
            return;
        }

    }

}
