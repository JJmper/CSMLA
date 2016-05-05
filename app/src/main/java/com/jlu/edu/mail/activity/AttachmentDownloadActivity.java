package com.jlu.edu.mail.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.adapter.AttachBaseAdapter;
import com.jlu.edu.mail.utils.CallOtherOpenfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import SQLite.MySQLiteImpl;

/**
 * Created by zhengheming on 2016/4/24.
 */
public class AttachmentDownloadActivity extends Activity {
    private TextView back;
    private ListView listView;
    private List<Map<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        init();
        initImpl();
    }


    private void init() {
        MySQLiteImpl mySQLite = new MySQLiteImpl();
        list = mySQLite.queryAttachment();
        back = (TextView) findViewById(R.id.attachment_back);
        listView = (ListView) findViewById(R.id.attachment_listview);
        AttachBaseAdapter adapter = new AttachBaseAdapter(AttachmentDownloadActivity.this, list);
        listView.setAdapter(adapter);
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
                Map<String, String> map = list.get(position);
                File file=new File( map.get("path"));
                CallOtherOpenfile callOtherOpenfile = new CallOtherOpenfile();
                callOtherOpenfile.openFile(AttachmentDownloadActivity.this,file);
            }
        });
    }

}
