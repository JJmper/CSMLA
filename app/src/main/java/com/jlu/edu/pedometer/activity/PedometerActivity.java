package com.jlu.edu.pedometer.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.main.MainActivity;
import com.jlu.edu.pedometer.service.SaveService;
import com.jlu.edu.pedometer.service.StepDetector;
import com.jlu.edu.pedometer.service.StepService;
import com.jlu.edu.pedometer.utils.CircleBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import SQLite.MySQLiteImpl;
import utils.SpUtil;

/**
 * 计步器基本功能
 * <p/>
 * 计步器相关设置在第四模块   灵敏度 步长 体重
 *
 * @author 郑和明 2016-2-20
 */
public class PedometerActivity extends Activity implements OnClickListener {
    private CircleBar circleBar;
    private TextView back;
    private ImageView history;
    private int total_step = 0;
    private Thread thread;
    private int Type = 1;
    private int calories = 0;
    private int step_length = 40;
    private int weight = 60;
    private SimpleDateFormat sdf;
    private String today;
    private MySQLiteImpl mySQLiteImpl;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            total_step = StepDetector.CURRENT_SETP;
            if (Type == 1) {
                circleBar.setProgress(total_step, Type);
            } else if (Type == 2) {
                calories = (int) (weight * total_step * step_length * 0.01 * 0.01);
                circleBar.setProgress(calories, Type);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
        init();
        mThread();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveDate();
    }


    private void saveDate() {
        //true 如果数据库中含有今天数据  更新数据
        //false 如果是数据库中没有今天数据  添加数据
        if (mySQLiteImpl.isPedometerData(today)) {
            mySQLiteImpl.updatePedometerData(total_step + "", today);
        } else {
            mySQLiteImpl.addPedometerData(total_step + "", today);
        }
    }


    private void init() {
        back = (TextView) findViewById(R.id.pedometer_back);
        history = (ImageView) findViewById(R.id.pedometer_history);
        circleBar = (CircleBar) findViewById(R.id.pedometer_progress_pedometer);
        //开启后台service
        Intent intent1 = new Intent(PedometerActivity.this, StepService.class);
        startService(intent1);
        Intent intent2 = new Intent(PedometerActivity.this, SaveService.class);
        startService(intent2);
        mySQLiteImpl = new MySQLiteImpl();
        sdf = new SimpleDateFormat("yyyyMMdd");
        today = sdf.format(new Date());
        //true 如果数据库中含有今天数据  读取数据
        //false 如果是数据库中没有今天数据  默认数据
        if (mySQLiteImpl.isPedometerData(today)) {
            Map<String, String> map = mySQLiteImpl.recePedometerData(today);
            StepDetector.CURRENT_SETP = Integer.valueOf(map.get("step"));
        } else {
            total_step = 0;
        }

        StepDetector.SENSITIVITY = new SpUtil("pedometer").send().getFloat("sensitivity", 5);
        step_length = new SpUtil("pedometer").send().getInt("step_length", 40);
        weight = new SpUtil("pedometer").send().getInt("weight", 60);
        circleBar.setMax(10000);
        circleBar.setProgress(StepDetector.CURRENT_SETP, 1);
        circleBar.startCustomAnimation();
        circleBar.setOnClickListener(this);
        back.setOnClickListener(this);
        history.setOnClickListener(this);
    }

    private void mThread() {
        if (thread == null) {

            thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (true) {
                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }
                    }
                }
            });
            thread.start();
        }
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.pedometer_back:
                Intent intent = new Intent(PedometerActivity.this, MainActivity.class);
                intent.putExtra("temp",2);
                startActivity(intent);
                finish();
                break;
            case R.id.pedometer_history:
                //跳转到历史
                Intent intent_history = new Intent(PedometerActivity.this, HistoryActivity.class);
                startActivity(intent_history);
                break;
            case R.id.pedometer_progress_pedometer:
                if (Type == 1) {
                    Type = 2;
                } else if (Type == 2) {
                    Type = 1;
                }
                Message msg = new Message();
                handler.sendMessage(msg);
                break;
            default:
                break;
        }
    }

}
