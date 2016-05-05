package utils;

/**
 * Application
 * Created by zhengheming on 2015/12/31.
 */

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.jlu.edu.mail.bean.MailInfo;
import com.jlu.edu.syllabus.domain.Course;
import com.jlu.edu.syllabus.service.TimerService;
import com.jlu.edu.syllabus.utils.MyLocationListenner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;
import javax.mail.Store;

public class MyApplication extends Application {

    private static Context context;
    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener;
    public static String TAG = "application";
    public static int week = 1;
    public static List<Course> list = null;
    public static Session session = null;
    private static Store store;
    public static MailInfo info = new MailInfo();
    private ArrayList<InputStream> attachmentsInputStreams;

    public void onCreate() {
        mLocationClient = new LocationClient(this);
        myListener = new MyLocationListenner(this, mLocationClient);
        mLocationClient.registerLocationListener(myListener);
        MyApplication.context = getApplicationContext();
        Intent intent = new Intent(MyApplication.this, TimerService.class);
        startService(intent);
        super.onCreate();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Intent intent = new Intent(MyApplication.this, TimerService.class);
        stopService(intent);
        Log.i("Application", "stop");
        super.onTerminate();
    }

    public static Store getStore() {
        return store;
    }

    public static void setStore(Store store) {
        MyApplication.store = store;
    }

    public ArrayList<InputStream> getAttachmentsInputStreams() {
        return attachmentsInputStreams;
    }

    public void setAttachmentsInputStreams(ArrayList<InputStream> attachmentsInputStreams) {
        this.attachmentsInputStreams = attachmentsInputStreams;
    }


    
}