package com.jlu.edu.pedometer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.pedometer.data.bean.PedometerData;

import java.util.List;

import SQLite.MySQLiteImpl;
import utils.SysActivity;

/**
 * 步行的历史纪录
 * Created by zhengheming on 2016/2/20.
 */
public class HistoryActivity extends Activity {
    private static final String TAG=HistoryActivity.class.getName();
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_history);
        SysActivity.getInstance().addActivity("HistoryActivity",HistoryActivity.this );
        init();
        inits();
    }

    private void init() {
        back= (TextView) findViewById(R.id.pedometer_history_back);
        List<PedometerData>  list=new MySQLiteImpl().recePedometerData();
        if(list.size()==0){
            Toast.makeText(HistoryActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
        }
        ListView  listView= (ListView) findViewById(R.id.pedometer_history_listview);
        listView.setAdapter(new History_BaseAdapter(this, list));
    }

    private void inits() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
