package com.jlu.edu.syllabus.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jlu.edu.syllabus.domain.Course;
import com.jlu.edu.syllabus.utils.SyllabusTimeChange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import utils.MyApplication;

/**
 * 后台时间计时器
 * 优化问题：
 * <p/>
 * 计时器优化---->撇弃Timer 采用ScheduledThreadPoolExecutor
 * 事件优化------>runnable中的事件简单化，清晰化，避免时间延时过长
 * 主要原因
 * 1、每次循环获取一次week数据(ko)
 * 2、每次循环都获取一次list课程数据(ko)
 * 3、每次循环都会将时间转成毫秒值，进而进行大小判断(ko)
 * 4、每次循环都会刷新静态数据
 * <p/>
 * 简化目标
 * 1、特定时间，特殊位置更新week
 * 2、尽量只获取一次list课程数据
 * 3、某些时间只转化一次
 * 4、必要时刷新静态数据
 * 添加功能
 * 1、闹铃     课程开始前特定时间提醒用户课程
 * 显示优化
 * <p/>
 * 现在    Thread类每一秒刷新数据
 * 目标    采用节省资源空间的方式--未知
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * Created by zhengheming on 2016/3/9.
 */
public class TimerService extends Service {
    private static final String TAG = TimerService.class.getName();

    public static String title;
    public static String syllabusname;
    public static String syllabustime;
    public static String syllabusplace;
    public static String syllabusteacher;
    private SimpleDateFormat sdft = null;
    private SimpleDateFormat sdfd = null;
    private SimpleDateFormat sdfs = null;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private Date date;

    @Override
    public void onCreate() {
        super.onCreate();
        this.init();

        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {

            public void run() {

                if (MyApplication.list!=null&&MyApplication.list.size() != 0) {
                    for (Course course : MyApplication.list) {
                        date = new Date();
                        Log.i("TimerService", course.getName());
                        long time = date.getTime();
                        long time_start = getLongTime(course.getStart(), 0);
                        long time_temp = getLongTime(course.getStart(), course.getStep());
                        Log.i("TimerService", "----" + time);
                        Log.i("TimerService", "----" + time_start);
                        Log.i("TimerService", "----" + time_temp);
                        if (time_start > time) {
                            Log.i("TimerService", "即将上课");
                            TimerService.title = "即将上课";
                            TimerService.syllabusname = course.getName();
                            TimerService.syllabustime = SyllabusTimeChange.Change(course.getStart());
                            TimerService.syllabusplace = course.getPlace();
                            TimerService.syllabusteacher = course.getTeacher();
                            break;
                        } else if (time_start <= time && time <= time_temp) {
                            Log.i("TimerService", "正在上课");
                            TimerService.title = "正在上课";
                            TimerService.syllabusname = course.getName();
                            TimerService.syllabustime = SyllabusTimeChange.Change(course.getStart());
                            TimerService.syllabusplace = course.getPlace();
                            TimerService.syllabusteacher = course.getTeacher();
                            break;
                        } else {
                            TimerService.title = "今天的课程都上完啦";
                        }
                    }

                } else {
                    TimerService.title = "今天没有课哦";
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    /**
     * 相关变量初始化
     */
    private void init() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
        sdft = new SimpleDateFormat("HH:mm");
        sdfd = new SimpleDateFormat("yyyy-MM-dd ");
        sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }


    /**
     * 获取最新系统时间
     *
     * @return
     */
    private String getTime() {
        return sdft.format(date);
    }

    private String getDate() {
        return sdfd.format(date);
    }

    private long getLongTime(int start, int step) {
        long res = 0;
        try {
            if (step == 0) {
                String d = getDate() + SyllabusTimeChange.Change(start);
                res = sdfs.parse(d).getTime();
            } else {
                String s = getDate() + SyllabusTimeChange.Change(start, step);
                res = sdfs.parse(s).getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //   res = res - 8 * 60 * 60 * 1000;

        return res;
    }

    @Override
    public ComponentName startService(Intent service) {
        Log.i(TAG, "TimeService->startService");
        return super.startService(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "TimeService->onDestroy");
    }

}
