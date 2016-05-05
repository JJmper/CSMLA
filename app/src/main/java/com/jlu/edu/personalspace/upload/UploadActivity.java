package com.jlu.edu.personalspace.upload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.utils.HttpUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import utils.LodingDialog;
import utils.MyApplication;
import utils.Net;
import utils.PatternAuth;
import utils.SpUtil;
import utils.SysActivity;
import utils.UrlPath;

/**
 * 上传/更改公共邮箱
 * Created by zhengheming on 2016/4/20.
 */
public class UploadActivity extends Activity implements View.OnClickListener, TextWatcher {
    private TextView title;
    private TextView subtitle;
    private EditText email_name;
    private EditText email_password;
    private EditText teacher_name;
    private Button cancel;
    private Button submit;
    private int type = 0;
    private Button clear_all;
    private LodingDialog dialog;
    private String emailname;
    private String emailpsd;
    private String teachername;
    private String Usernumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getData();
        init();
        initImpl();

    }

    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
    }

    private void init() {
        SysActivity.getInstance().addActivity("UpLoadActivity", this);
        title = (TextView) findViewById(R.id.upload_title);
        subtitle = (TextView) findViewById(R.id.upload_struction);
        email_name = (EditText) findViewById(R.id.upload_email_name);
        email_password = (EditText) findViewById(R.id.upload_email_password);
        teacher_name = (EditText) findViewById(R.id.upload_email_teacher_name);
        cancel = (Button) findViewById(R.id.upload_email_cancel);
        submit = (Button) findViewById(R.id.upload_email_upload);
        clear_all = (Button) findViewById(R.id.clear_all);
        dialog = new LodingDialog(UploadActivity.this);
        Usernumber = new SpUtil("User").send().getString("Usernumber", "");
    }

    private void initImpl() {
        if (type == 0) {
            title.setText("上传公共邮箱");
            subtitle.setText("邮箱上传说明");
            submit.setText("上传");
        } else {
            title.setText("更新公共邮箱");
            subtitle.setText("邮箱更新说明");
            submit.setText("更新");
        }
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        email_name.addTextChangedListener(this);
        clear_all.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_email_cancel:
                finish();
                break;
            case R.id.upload_email_upload:
                loginEmails();
                break;
            case R.id.clear_all:
                email_name.setText("");
                break;
        }
    }

    private void upload() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.addEmails + "?number=" + Usernumber + "&name=" + teachername + "&emailname=" + emailname + "&emailpwd=" + emailpsd;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);

                if ("1".equals(res)) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = emailname;
                    msg.arg1 = 0;
                    handler.sendMessage(msg);
                } else if ("2".equals(res)) {
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = "已上传，请不要重复上传";
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = "上传失败";
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = "服务器连接失败";
                handler.sendMessage(msg);
            }
        });
    }

    private void update() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.updateEmails + "?number=" + Usernumber + "&name=" + teachername + "&emailname=" + emailname + "&emailpwd=" + emailpsd;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);

                if ("1".equals(res)) {
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = emailname;
                    msg.arg1 = 1;
                    handler.sendMessage(msg);
                } else if ("2".equals(res)) {
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = "您上次上传的就是该邮箱，请不要重复上传";
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = "更新失败";
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = "服务器连接失败";
                handler.sendMessage(msg);
            }
        });
    }

    private void loginEmails() {
        if (!new Net().isNetworkAvailable(UploadActivity.this)) {
            Toast.makeText(UploadActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
        emailname = email_name.getText().toString().trim();
        emailpsd = email_password.getText().toString().trim();
        teachername = teacher_name.getText().toString().trim();
        switch (0) {
            case 0:
                if (TextUtils.isEmpty(emailname)) {
                    Toast.makeText(UploadActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

            case 1:
                if (!PatternAuth.email(emailname)) {
                    Toast.makeText(UploadActivity.this, "账号不符合邮箱地址规则", Toast.LENGTH_SHORT).show();
                    return;
                }
            case 2:
                if (!(PatternAuth.getemail(emailname, "@163") || PatternAuth.getemail(emailname, "@qq"))) {
                    Toast.makeText(UploadActivity.this, "目前只支持163邮箱或者qq邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }

            case 3:
                if (TextUtils.isEmpty(emailpsd)) {
                    Toast.makeText(UploadActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            case 4:
                if (TextUtils.isEmpty(teachername)) {
                    Toast.makeText(UploadActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
        }
        String host;
        String port;
        String type;
        if (PatternAuth.getemail(emailname, "@163")) {
            host = "pop3.163.com";
            port = "110";
            type = "pop3";
        } else if (PatternAuth.getemail(emailname, "@qq")) {
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
        MyApplication.info.setUserName(emailname);
        MyApplication.info.setPassword(emailpsd);
        dialog.show();
        new Thread() {
            @Override
            public void run() {
                //登入操作
                HttpUtil util = new HttpUtil();
                MyApplication.session = util.login();
                handler.sendEmptyMessage(0);
            }

        }.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:

                    if (MyApplication.session == null) {
                        dialog.dismiss();
                        Toast.makeText(UploadActivity.this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        if (type == 0) {
                            upload();
                        } else {
                            update();
                        }
                    }
                    break;
                case 1:
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, "上传公共邮箱成功", Toast.LENGTH_SHORT).show();
                    SysActivity.getInstance().exit("FinishingActivity");
                    Intent upload = new Intent(UploadActivity.this, FinishingActivity.class);
                    upload.putExtra("emailname", (String) msg.obj);
                    upload.putExtra("emailtype", msg.arg1);
                    startActivity(upload);
                    finish();
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, "更新公共邮箱成功", Toast.LENGTH_SHORT).show();
                    SysActivity.getInstance().exit("FinishingActivity");
                    Intent update = new Intent(UploadActivity.this, FinishingActivity.class);
                    update.putExtra("emailname", (String) msg.obj);
                    update.putExtra("emailtype", msg.arg1);
                    startActivity(update);
                    finish();
                    break;
                case 3:
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            clear_all.setVisibility(View.VISIBLE);
        } else {
            clear_all.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
