package com.jlu.edu.mail.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.adapter.AttachmentBaseAdapter;
import com.jlu.edu.mail.bean.Email;
import com.jlu.edu.mail.utils.IOUtil;
import com.jlu.edu.mail.view.NoScrollListView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import SQLite.MySQLiteImpl;
import utils.MyApplication;

/**
 * Created by zhengheming on 2016/4/10.
 */
public class MailsBoxDetailsActivity extends Activity {
    private static final String TAG = MailsBoxDetailsActivity.class.getName();
    private TextView back;
    private TextView title;
    private TextView addresser;
    private TextView recepients;
    private TextView time;
    private TextView content;
    private WebView html;
    private ArrayList<InputStream> attachmentsInputStreams;
    private Email email;
    private MyHandler handler;
    private MySQLiteImpl mySQLiteImpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailsbox_details);
        getData();
        init();
        initImpl();


    }

    private void getData() {
        email = (Email) getIntent().getSerializableExtra("EMAIL");
        attachmentsInputStreams = ((MyApplication) getApplication()).getAttachmentsInputStreams();
    }

    private void init() {
        back = (TextView) findViewById(R.id.mailsbox_details_back);
        title = (TextView) findViewById(R.id.mailsbox_details_title);
        addresser = (TextView) findViewById(R.id.mailsbox_details_addresser);
        recepients = (TextView) findViewById(R.id.mailsbox_details_recepients);
        time = (TextView) findViewById(R.id.mailsbox_details_time);
        content = (TextView) findViewById(R.id.mailsbox_details_text);
        html = (WebView) findViewById(R.id.mailsbox_details_html);
        handler = new MyHandler(this);
        mySQLiteImpl=new MySQLiteImpl();
    }

    private void initImpl() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(email.getSubject());
        addresser.setText(email.getAddressser());
        recepients.setText(email.getRecepients());
        time.setText(email.getTime());
        if (email.isHtml()) {
            html.loadDataWithBaseURL(null, email.getContent(), "text/html", "utf-8", null);
            //设置缩放
            html.getSettings().setBuiltInZoomControls(true);
            //网页适配
            DisplayMetrics dm = getResources().getDisplayMetrics();
            int scale = dm.densityDpi;
            if (scale == 240) {
                html.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            } else if (scale == 160) {
                html.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
            } else {
                html.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
            }
            html.setWebChromeClient(new WebChromeClient());
            content.setVisibility(View.GONE);
        } else {

            content.setText(email.getContent());
            content.setVisibility(View.VISIBLE);
            html.setVisibility(View.GONE);
        }
        if (email.getAttach().size() > 0) {
            NoScrollListView listView;
            AttachmentBaseAdapter adapter;
            listView = (NoScrollListView) findViewById(R.id.mailsbox_details_attachment);

            adapter = new AttachmentBaseAdapter(MailsBoxDetailsActivity.this, email.getAttach());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            handler.obtainMessage(0, "开始下载【" + email.getAttach().get(position) + "】").sendToTarget();
                            InputStream is = attachmentsInputStreams.get(position);
                            String path = new IOUtil().stream2file(is, Environment.getExternalStorageDirectory().toString() + "/temp/" + email.getAttach().get(position));
                            if (path == null) {
                                handler.obtainMessage(0, "下载失败！").sendToTarget();
                            } else {
                                mySQLiteImpl.addAttachment(path,email.getAttach().get(position));
                                handler.obtainMessage(0, "文件保存在：" + path).sendToTarget();

                            }
                        }
                    }).start();
                }
            });
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<MailsBoxDetailsActivity> wrActivity;

        public MyHandler(MailsBoxDetailsActivity activity) {
            this.wrActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            final MailsBoxDetailsActivity activity = wrActivity.get();
            switch (msg.what) {
                case 0:
                    Toast.makeText(activity.getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    }

    ;

}
