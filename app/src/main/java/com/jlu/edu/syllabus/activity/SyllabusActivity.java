package com.jlu.edu.syllabus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.syllabus.custom.CustomProgressDialog;
import com.jlu.edu.syllabus.domain.Course;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import SQLite.MySQLiteImpl;
import utils.DensityUtil;
import utils.SpUtil;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/2/29.
 */
public class SyllabusActivity extends Activity {
    private static final String TAG = SyllabusActivity.class.getName();
    private LinearLayout weekPanels[] = new LinearLayout[7];
    private LinearLayout root;
    private TextView back;
    private TextView add;
    private List<Course> courseData[] = new ArrayList[7];
    private int itemHeight;
    private int marTop, marLeft;
    private CustomProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        init();
        itemHeight = getResources().getDimensionPixelSize(R.dimen.weekItemHeight);
        marTop = getResources().getDimensionPixelSize(R.dimen.weekItemMarTop);
        marLeft = getResources().getDimensionPixelSize(R.dimen.weekItemMarLeft);
        getData();
    }

    private void dataImpl(Map<Integer, List<Course>> map) {
        for (int i = 0; i < 7; i++) {
            courseData[i] = map.get(i + 1);
        }
        for (int i = 0; i < weekPanels.length; i++) {
            weekPanels[i] = (LinearLayout) findViewById(R.id.syllabus_weekPanel_1 + i);
            initWeekPanel(weekPanels[i], courseData[i]);
        }
    }

    private void init() {
        root = (LinearLayout) findViewById(R.id.syllabus_contentPanel);
        back = (TextView) findViewById(R.id.syllabus_back);
        add = (TextView) findViewById(R.id.syllabus_add);
        back.setOnClickListener(MyOnClickListener);
        add.setOnClickListener(MyOnClickListener);
    }

    public void getData() {
        Map<Integer, List<Course>> map = new MySQLiteImpl().querySyllabus();

        if ((map.size() == 1) && (map.get(0) == null)) {
            Log.i(TAG, "map=0");
            startProgressDialog();
            Log.i(TAG, "对话框开启");
            synData();
        } else {
            Log.i(TAG, "map!=0");
            dataImpl(map);
        }


    }

    public void initWeekPanel(LinearLayout linearLayout, List<Course> data) {
        if (linearLayout == null || data == null || data.size() < 1) return;
        Course pre = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            Course c = data.get(i);
            final TextView tv = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    itemHeight * c.getStep() + marTop * (c.getStep() - 1));
            if (i > 0) {
                lp.setMargins(marLeft, (c.getStart() - (pre.getStart() + pre.getStep())) * (itemHeight + marTop) + marTop, 0, 0);
            } else {
                lp.setMargins(marLeft, (c.getStart() - 1) * (itemHeight + marTop) + marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setId(c.getWeek());
            tv.setTextColor(getResources().getColor(R.color.courseTextColor));
            tv.setText(c.getName() + "\n" + c.getPlace() + "\n" + c.getTeacher());
            tv.setBackground(getResources().getDrawable(R.drawable.bg_circle));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = tv.getText().toString();
                    int week = tv.getId();
                    int start = getStart(SyllabusActivity.this, tv.getTop());
                    int step = getStep(SyllabusActivity.this, tv.getTop(), tv.getBottom());
                    Intent intent = new Intent(SyllabusActivity.this, UpdateOrAddSyllabusActivity.class);
                    intent.putExtra("TYPE", 1);
                    intent.putExtra("text", text);
                    intent.putExtra("week", week);
                    intent.putExtra("start", start);
                    intent.putExtra("step", step);
                    Toast.makeText(SyllabusActivity.this, start + "----" + step, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            });
            linearLayout.addView(tv);
            pre = c;
        }


    }

    //获取textview  start数据
    private int getStart(Context context, int top) {
        top = DensityUtil.px2dip(context, top);
        int res = (top - 2) / 42 + 1;
        return res;
    }
    //获取textview  step数据

    private int getStep(Context context, int top, int bottom) {
        top = DensityUtil.px2dip(context, top);
        bottom = DensityUtil.px2dip(context, bottom);
        int plus = bottom - top;
        int res = (plus + 2) / 42;
        return res;
    }


    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.syllabus_back:
                    finish();
                    break;
                case R.id.syllabus_add:
                    Intent intent = new Intent(SyllabusActivity.this, UpdateOrAddSyllabusActivity.class);
                    intent.putExtra("TYPE", 0);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    //同步
    private void synData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.QuerySyllabusServlet+"?usernumber="+new SpUtil("User").send().getString("Usernumber","");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int s, Header[] headers, byte[] bytes) {

                String res = new String(bytes);

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("syllabus");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String name = item.getString("name");
                        String teacher = item.getString("teacher");
                        String place = item.getString("place");
                        int week = item.getInt("week");
                        int start = item.getInt("start");
                        int step = item.getInt("step");
                        Course course = new Course(name, teacher, place, week, start, step);
                        new MySQLiteImpl().addSyllabus(course);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    handler.sendEmptyMessage(0);
                }


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(SyllabusActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "获取完网络数据");
            Map<Integer, List<Course>> map = new MySQLiteImpl().querySyllabus();
            stopProgressDialog();
            dataImpl(map);

        }
    };
    private void startProgressDialog(){
        if (progressDialog == null){
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在同步数据中...");
        }

        progressDialog.show();
    }
    private void stopProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}
