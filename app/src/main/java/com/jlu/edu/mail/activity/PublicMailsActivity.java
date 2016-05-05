package com.jlu.edu.mail.activity;

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
import com.jlu.edu.mail.adapter.PublicMailsBaseAdapter;
import com.jlu.edu.mail.bean.Emails;
import com.jlu.edu.mail.utils.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.LodingDialog;
import utils.MyApplication;
import utils.Net;
import utils.PatternAuth;
import utils.SpUtil;
import utils.SysActivity;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/4/9.
 */
public class PublicMailsActivity extends Activity {
    private static final String TAG = PublicMailsActivity.class.getName();
    private ListView listView;
    private TextView text;
    private TextView back;
    private PublicMailsBaseAdapter adapter;
    private List<Emails> list = new ArrayList<>();
    private LodingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicmails);
        init();
        initImpl();
    }


    private void init() {

        SysActivity.getInstance().addActivity("PublicMailsActivity", this);
        listView = (ListView) findViewById(R.id.publicmails_listview);
        text = (TextView) findViewById(R.id.publicmails_text);
        back = (TextView) findViewById(R.id.publicmails_back);
        dialog = new LodingDialog(PublicMailsActivity.this);
        adapter = new PublicMailsBaseAdapter(PublicMailsActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.show();
                Emails emails = list.get(position);
                loginEmails(emails.getEmailname(), emails.getEmailpwd());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initImpl() {
        if (!new Net().isNetworkAvailable(PublicMailsActivity.this)) {
            Toast.makeText(PublicMailsActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        queryEmails();
    }

    private void queryEmails() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.queryEmails+"?number="+new SpUtil("User").send().getString("Usernumber","");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int r, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("0".equals(res)) {
                    handler.sendEmptyMessage(0);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        JSONArray jsonArray = jsonObject.getJSONArray("email");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            Emails emails = new Emails();
                            emails.setTeachername(json.getString("teachername"));
                            emails.setEmailname(json.getString("emailname"));
                            emails.setEmailpwd(json.getString("emailpwd"));
                            emails.setAccount(json.getInt("account"));
                            list.add(emails);
                        }
                        handler.sendEmptyMessage(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                handler.sendEmptyMessage(2);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismiss();
                    listView.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    dialog.dismiss();
                    listView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                    adapter.dataOnchange(list);
                    break;
                case 2:
                    dialog.dismiss();
                    listView.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    if (MyApplication.session == null) {
                        dialog.dismiss();
                        Toast.makeText(PublicMailsActivity.this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Intent intent = new Intent(PublicMailsActivity.this, MailsActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(PublicMailsActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }

        }
    };

    private void loginEmails(String emailsname, String emailspassword) {
        String host;
        String port;
        String type;
        if (PatternAuth.getemail(emailsname, "@163")) {
            host = "pop3.163.com";
            port = "110";
            type = "pop3";
        } else if (PatternAuth.getemail(emailsname, "@qq")) {
            host = "imap.qq.com";
            port = "993";
            type = "imap";
        } else {
            host = "";
            port = "";
            type = "";
        }
        MyApplication.info.setType(type);
        MyApplication.info.setMailServerHost(host);
        MyApplication.info.setMailServerPort(port);
        MyApplication.info.setUserName(emailsname);
        MyApplication.info.setPassword(emailspassword);
        new Thread() {
            @Override
            public void run() {
                //登入操作
                HttpUtil util = new HttpUtil();
                MyApplication.session = util.login();
                handler.sendEmptyMessage(3);
            }

        }.start();

    }
}
