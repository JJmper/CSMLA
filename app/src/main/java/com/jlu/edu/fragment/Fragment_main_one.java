package com.jlu.edu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.syllabus.activity.SyllabusActivity;
import com.jlu.edu.syllabus.service.TimerService;
import com.jlu.edu.syllabus.utils.MyLocationListenner;
import com.jlu.edu.syllabus.utils.StartLocationService;
import com.jlu.edu.syllabus.utils.SyllabusTimeChange;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import SQLite.MySQLiteImpl;
import utils.MyApplication;
import utils.PatternAuth;
import utils.SpUtil;
import utils.UrlPath;

/**
 * 点名系统  框架构造/
 * Created by zhengheming on 2016/1/10.
 */
public class Fragment_main_one extends Fragment {
    private static final String TAG = Fragment_main_one.class.getName();
    private View view;

    private LinearLayout hasCourse;
    private LinearLayout noCourse;
    private TextView courseName;
    private TextView courseTime;
    private TextView named;
    private TextView title_time;
    private TextView courseplace;
    private TextView oncourse_title;
    private EditText auth;
    private ImageView submit;
    private ImageView courseTable;
    private String classify;
    private String usernumber;
    private Thread thread;
    private double lon;
    private double lat;
    private StartLocationService startLocationService = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_one, container, false);
        getData();
        init();
        initimpl();
        mThread();
        return view;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (TimerService.title == null||"今天的课程都上完啦".equals(TimerService.title)||"今天没有课哦".equals(TimerService.title)) {
                noCourse.setVisibility(View.VISIBLE);
                hasCourse.setVisibility(View.GONE);
                oncourse_title.setText(TimerService.title);
            } else {
                noCourse.setVisibility(View.GONE);
                hasCourse.setVisibility(View.VISIBLE);
                courseName.setText(TimerService.syllabusname.trim());
                courseTime.setText(TimerService.syllabustime.trim());
                courseplace.setText(TimerService.syllabusplace.trim());
                title_time.setText(TimerService.title);
                startLocationService.StartLocation();
                lon = MyLocationListenner.longitude;
                lat = MyLocationListenner.latitude;
            }


        }
    };

    private void getData() {

        usernumber = new SpUtil("User").send().getString("Usernumber", "");
        if ("".equals(usernumber)) {
            Toast.makeText(getActivity(), "暂未登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        classify = new MySQLiteImpl().receUserData(usernumber).getUserclassify();

    }


    private void init() {
        hasCourse = (LinearLayout) view.findViewById(R.id.main_one_hascourse);
        noCourse = (LinearLayout) view.findViewById(R.id.main_one_nocourse);
        courseName = (TextView) view.findViewById(R.id.main_one_name);
        courseTime = (TextView) view.findViewById(R.id.main_one_time);
        courseplace = (TextView) view.findViewById(R.id.main_one_place);
        title_time = (TextView) view.findViewById(R.id.main_one_time_title);
        oncourse_title = (TextView) view.findViewById(R.id.main_one_nocourse_title);
        named = (TextView) view.findViewById(R.id.main_one_named);
        if (TimerService.title == null||"今天的课程都上完啦".equals(TimerService.title)||"今天没有课哦".equals(TimerService.title)) {
            noCourse.setVisibility(View.VISIBLE);
            hasCourse.setVisibility(View.GONE);
        } else {
            noCourse.setVisibility(View.GONE);
            hasCourse.setVisibility(View.VISIBLE);
            courseName.setText(TimerService.syllabusname.trim());
            courseTime.setText(TimerService.syllabustime.trim());
            courseplace.setText(TimerService.syllabusplace.trim());
            title_time.setText(TimerService.title);
            startLocationService.StartLocation();
            lon = MyLocationListenner.longitude;
            lat = MyLocationListenner.latitude;
        }
        if ("teacher".equals(classify)) {
            named.setText("教师发送验证码");
        } else {
            named.setText("学生发送验证码");
        }
        auth = (EditText) view.findViewById(R.id.main_one_auth);
        submit = (ImageView) view.findViewById(R.id.main_one_submit);
        courseTable = (ImageView) view.findViewById(R.id.main_one_table);
        startLocationService = StartLocationService.GetInstent(getActivity().getApplication(), startLocationService);
    }



    private void initimpl() {
        //课程表入口动画效果
        TranslateAnimation animation = new TranslateAnimation(5, -5, 5, -5);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(400);
        animation.setRepeatCount(100);
        animation.setRepeatMode(Animation.REVERSE);
        courseTable.startAnimation(animation);
        submit.setOnClickListener(myOnClickListener);
        courseTable.setOnClickListener(myOnClickListener);
    }

    View.OnClickListener myOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_one_submit:
                    if ("正在上课".equals(TimerService.title)) {
                        String auth_ = auth.getText().toString();
                        if (PatternAuth.getAuth(auth_, "[0-9]{4}")) {
                            Http_Auth(auth_);
                        }

                    } else {

                        Toast.makeText(getActivity(), "不在上课时间", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.main_one_table:
                    Intent intent = new Intent(getActivity(), SyllabusActivity.class);
                    getActivity().startActivity(intent);

                    break;
                default:
                    break;
            }
        }
    };

    private void mThread() {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                            Message msg = new Message();
                            handler.sendMessage(msg);

                    }
                }
            });
            thread.start();
        }
    }

    private void Http_Auth(String auth) {
        String url;
        if ("student".equals(classify)) {
            url = UrlPath.AddStudentStatistics;
        } else {
            url = UrlPath.AddTeacherStatistics;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("usernumber", usernumber);
        params.put("syllabusname", TimerService.syllabusname);
        params.put("syllabusteacher", TimerService.syllabusteacher);
        params.put("auth", auth);
        params.put("longitude", lon);
        params.put("latitude", lat);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onStart() {
        MyApplication.week = SyllabusTimeChange.getWeek();
        MyApplication.list = new MySQLiteImpl().querySyllabus(MyApplication.week);
        super.onStart();
    }


}
