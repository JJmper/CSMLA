package com.jlu.edu.personalspace.statistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
 * 获取教师点名统计信息
 * Created by zhengheming on 2016/4/1.
 */
public class StatisticsActivity extends Activity {
    private TextView back;
    private TextView text;
    private ListView listView;
    private List<String> list = new ArrayList<>();
    private LodingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        init();
        initData();
        initImpl();
    }

    private void initImpl() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StatisticsActivity.this, StatisticsDetailsActivity.class);
                intent.putExtra("syllabus", list.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        syllabus();
    }

    private void init() {
        back = (TextView) findViewById(R.id.statistics_back);
        text = (TextView) findViewById(R.id.statistics_text);
        listView = (ListView) findViewById(R.id.statistics_listview);
        dialog = new LodingDialog(StatisticsActivity.this);
    }

    private void syllabus() {
        String usernumber = new SpUtil("User").send().getString("Usernumber", "");
        if ("".equals(usernumber)) {
            Toast.makeText(StatisticsActivity.this, "未登录", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.querySimpleSyllabus + "?usernumber=" + usernumber;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int r, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("".equals(res)) {
                    handler.sendEmptyMessage(1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("syllabus");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String name = jsonArray.getString(i);
                        list.add(name);
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
                handler.sendEmptyMessage(0);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    text.setText("网络异常");
                    text.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    dialog.dismiss();
                    break;
                case 1:
                    text.setText("暂时没有课程收录");
                    text.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    dialog.dismiss();
                    break;
                case 2:
                    text.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    StatisticsAdapter adapter;
                    adapter = new StatisticsAdapter(StatisticsActivity.this, list);
                    listView.setAdapter(adapter);
                    dialog.dismiss();
                    break;
                case 3:
                    break;
            }
        }
    };
}
