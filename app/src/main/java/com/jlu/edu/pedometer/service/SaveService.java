package com.jlu.edu.pedometer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import SQLite.MySQLiteImpl;
import utils.ChangeType;

public class SaveService extends Service {
    private int S_step;
    private String time;
    private String today;
    private MySQLiteImpl mySQLiteImpl;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);

    }


    private void init() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd", Locale.CHINESE);
        SimpleDateFormat sdf2 = new SimpleDateFormat("hhmmss",Locale.CHINESE);
        Date date = new Date();
        today = sdf1.format(date);
        time = sdf2.format(date);
        mySQLiteImpl = new MySQLiteImpl();
        new Thread(new MyThread()).start();
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000);// 线程暂停1秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!"000000".equals(time)) {
                return;
            }
          String  yestoday = String.valueOf(Integer.valueOf(today) - 1);

            if (mySQLiteImpl.isPedometerData(String.valueOf(Integer.valueOf(today) - 1))) {
                if (ChangeType.change_S_I(mySQLiteImpl.recePedometerData(yestoday).get("step")) <= StepDetector.CURRENT_SETP) {
                    S_step = StepDetector.CURRENT_SETP;
                } else {
                    S_step = ChangeType.change_S_I(mySQLiteImpl.recePedometerData(yestoday).get("step"));
                }
                mySQLiteImpl.updatePedometerData(S_step, yestoday);
            } else {
                mySQLiteImpl.addPedometerData(StepDetector.CURRENT_SETP, yestoday);
            }


            super.handleMessage(msg);
        }
    };
}
