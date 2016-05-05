package com.jlu.edu.personalspace.statistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.LodingDialog;
import utils.SpUtil;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/4/1.
 */
public class StatisticsDetailsActivity extends Activity {
    private List<Statistics_record> list = new ArrayList<>();
    private ListView listView;
    private TextView back;
    private TextView syllabus;
    private LodingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_details);
        init();
        initImpl();
    }


    private void init() {
        back = (TextView) findViewById(R.id.statistics_details_back);
        syllabus = (TextView) findViewById(R.id.statistics_details_syllabus);
        listView = (ListView) findViewById(R.id.statistics_details_listview);
        dialog = new LodingDialog(StatisticsDetailsActivity.this);
    }

    private void initImpl() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String usernumber = new SpUtil("User").send().getString("Usernumber", "");
        Intent intent = getIntent();
        String name = intent.getStringExtra("syllabus");
        syllabus.setText(name);
        queryStatistics(usernumber, name);
    }

    private void queryStatistics(String usernumber, String syllabusname) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.ReceStatistics + "?usernumber=" + usernumber + "and syllabusname=" + syllabusname;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int r, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("statistics");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String usernumber = item.getString("usernumber");
                        String name = item.getString("name");
                        String teacher = item.getString("teacher");
                        String student = item.getString("student");
                        int count = item.getInt("count");
                        Statistics_record record = new Statistics_record();
                        record.setUsernumber(usernumber);
                        record.setName(name);
                        record.setStudent(student);
                        record.setTeacher(teacher);
                        record.setCount(count);
                        list.add(record);
                    }
                    if (list.size() == 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(StatisticsDetailsActivity.this, "暂时没有数据", Toast.LENGTH_SHORT).show();
                    break;
                case 2:

                    break;
            }
            dialog.dismiss();
            StatisticsDetailsAdapter adapter;
            adapter = new StatisticsDetailsAdapter(StatisticsDetailsActivity.this, list);
            listView.setAdapter(adapter);
        }
    };

}
