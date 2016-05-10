package com.jlu.edu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.personalspace.aboutus.AboutUsActivity;
import com.jlu.edu.personalspace.feedback.FeedBackActivity;
import com.jlu.edu.personalspace.pedometer.PedometerSetting;
import com.jlu.edu.personalspace.personalmessage.PersonalMessage;
import com.jlu.edu.personalspace.statistics.StatisticsActivity;
import com.jlu.edu.personalspace.upload.FinishingActivity;
import com.jlu.edu.personalspace.upload.UploadActivity;
import com.jlu.edu.personalspace.wordbook.WordsBookActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import SQLite.MySQLiteImpl;
import utils.LodingDialog;
import utils.SpUtil;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/1/10.
 */
public class Fragment_main_four extends Fragment {

    private View view;
    private LinearLayout course_check_;
    private LinearLayout upload_;
    private TextView personalmessage;
    private TextView pedometer;
    private TextView wordbook;
    private TextView course_check;
    private TextView upload;
    private TextView checkupdate;
    private TextView suggest;
    private TextView about_us;
    private TextView exit;
    private String classify;
    private LodingDialog dialog;
    private int temp=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_four, container, false);
        initData();
        init();
        return view;
    }

    private void initData() {
        String usernumber = new SpUtil("User").send().getString("Usernumber", "");
        classify = new MySQLiteImpl().receUserData(usernumber).getUserclassify();
    }

    private void init() {
        course_check_ = (LinearLayout) view.findViewById(R.id.main_four_course_check_);
        upload_ = (LinearLayout) view.findViewById(R.id.main_four_upload_);
        if ("student".equals(classify)) {
            course_check_.setVisibility(View.GONE);
            upload_.setVisibility(View.GONE);
        } else {
            course_check_.setVisibility(View.VISIBLE);
            upload_.setVisibility(View.VISIBLE);
        }
        personalmessage = (TextView) view.findViewById(R.id.main_four_personal);
        pedometer = (TextView) view.findViewById(R.id.main_four_pedometer_setting);
        wordbook = (TextView) view.findViewById(R.id.main_four_wordbook);
        course_check = (TextView) view.findViewById(R.id.main_four_course_check);
        upload = (TextView) view.findViewById(R.id.main_four_upload);
        checkupdate = (TextView) view.findViewById(R.id.main_four_check_update);
        suggest = (TextView) view.findViewById(R.id.main_four_suggest);
        about_us = (TextView) view.findViewById(R.id.main_four_about_us);
        exit = (TextView) view.findViewById(R.id.main_four_system_exit);
        personalmessage.setOnClickListener(MyOnClickListener);
        pedometer.setOnClickListener(MyOnClickListener);
        wordbook.setOnClickListener(MyOnClickListener);
        course_check.setOnClickListener(MyOnClickListener);
        upload.setOnClickListener(MyOnClickListener);
        checkupdate.setOnClickListener(MyOnClickListener);
        suggest.setOnClickListener(MyOnClickListener);
        about_us.setOnClickListener(MyOnClickListener);
        exit.setOnClickListener(MyOnClickListener);
        dialog = new LodingDialog(getActivity());

    }

    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_four_personal:
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), PersonalMessage.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.main_four_pedometer_setting:
                    Intent intent = new Intent(getActivity(), PedometerSetting.class);
                    startActivity(intent);
                    break;
                case R.id.main_four_wordbook:
                    if (getActivity() != null) {
                        Intent pedo = new Intent(getActivity(), WordsBookActivity.class);
                        startActivity(pedo);
                    }
                    break;
                case R.id.main_four_course_check:
                    Intent statistics = new Intent(getActivity(), StatisticsActivity.class);
                    startActivity(statistics);
                    break;
                case R.id.main_four_upload:
                    dialog.show();
                    upload();
                    break;
                case R.id.main_four_check_update:
                    Toast.makeText(getActivity(), "此软件已是最新版本", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.main_four_suggest:
                    Intent feedback = new Intent(getActivity(), FeedBackActivity.class);
                    startActivity(feedback);
                    break;
                case R.id.main_four_about_us:
                    Intent about = new Intent(getActivity(), AboutUsActivity.class);
                    startActivity(about);
                    break;
                case R.id.main_four_system_exit:

                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    };

    private void upload() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.isEmails+"?number="+new SpUtil("User").send().getString("Usernumber","");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("0".equals(res)) {
                    handler.sendEmptyMessage(0);
                } else {
                    try {
                        Message msg = new Message();
                        JSONObject jsonObject = new JSONObject(res);
                        JSONObject json = jsonObject.getJSONObject("email");
                        msg.obj = json.getString("emailname");
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
               Log.i("Fragment_four","error");
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismiss();
                    Intent upload = new Intent(getActivity(), UploadActivity.class);
                    upload.putExtra("type",0);
                    startActivity(upload);
                    break;
                case 1:
                    dialog.dismiss();
                    Intent update = new Intent(getActivity(), FinishingActivity.class);
                    update.putExtra("emailname", (String) msg.obj);
                    startActivity(update);
                    break;
            }
        }
    };
}
