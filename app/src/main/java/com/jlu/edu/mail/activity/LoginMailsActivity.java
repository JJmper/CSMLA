package com.jlu.edu.mail.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.utils.HttpUtil;

import utils.LodingDialog;
import utils.MyApplication;
import utils.Net;
import utils.PatternAuth;
import utils.SpUtil;

/**
 * 邮箱登陆
 * <p/>
 * 获取全部邮件信息
 * <p/>
 * 详细信息
 * <p/>
 * 点击下载
 * <p/>
 * 加载到下载中
 * <p/>
 * <p/>
 * <p/>
 * Created by zhengheming on 2016/4/6.
 */
public class LoginMailsActivity extends Activity implements TextWatcher {
    private static final String TAG = LoginMailsActivity.class.getName();
    private EditText account;
    private EditText password;
    private Button cancel;
    private Button login;
    private Button clear_all;
    private String username;
    private String userpsd;
    private CheckBox rememberpsd;
    private SharedPreferences sp;
    private LodingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmails);
        init();
        initImpl();
        isRemenberPwd();
    }


    private void init() {
        account = (EditText) findViewById(R.id.loginmails_account);
        password = (EditText) findViewById(R.id.loginmails_password);
        cancel = (Button) findViewById(R.id.loginmails_cancel);
        login = (Button) findViewById(R.id.loginmails_login);
        clear_all = (Button) findViewById(R.id.clear_all);
        rememberpsd = (CheckBox) findViewById(R.id.rememberPassword);
        sp = new SpUtil("Email").send();
    }

    private void initImpl() {
        cancel.setOnClickListener(MyOnClickListener);
        login.setOnClickListener(MyOnClickListener);
        account.addTextChangedListener(this);
        clear_all.setOnClickListener(MyOnClickListener);
        rememberpsd.setOnClickListener(MyOnClickListener);

    }

    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.clear_all:
                    account.setText("");
                    break;
                case R.id.rememberPassword:
                    remenberPwd();
                    break;
                case R.id.loginmails_cancel:
                    finish();
                    break;
                case R.id.loginmails_login:
                    loginEmails();
                    break;
            }
        }
    };

    private void loginEmails() {
        if (!new Net().isNetworkAvailable(LoginMailsActivity.this)) {
            Toast.makeText(LoginMailsActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
        username = account.getText().toString().trim();
        userpsd = password.getText().toString().trim();
        switch (0) {
            case 0:
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginMailsActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

            case 1:
                if (!PatternAuth.email(username)) {
                    Toast.makeText(LoginMailsActivity.this, "账号不符合邮箱地址规则", Toast.LENGTH_SHORT).show();
                    return;
                }
            case 2:
                if (!(PatternAuth.getemail(username, "@163") || PatternAuth.getemail(username, "@qq"))) {
                    Toast.makeText(LoginMailsActivity.this, "目前只支持163邮箱或者qq邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }

            case 3:
                if (TextUtils.isEmpty(userpsd)) {
                    Toast.makeText(LoginMailsActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
        }
        String host;
        String port;
        String type;
        if (PatternAuth.getemail(username, "@163")) {
            host = "pop3.163.com";
            port = "110";
            type = "pop3";
        } else if (PatternAuth.getemail(username, "@qq")) {
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
        MyApplication.info.setUserName(username);
        MyApplication.info.setPassword(userpsd);
        dialog = new LodingDialog(LoginMailsActivity.this);
        dialog.show();
        new Thread() {
            @Override
            public void run() {
                //登入操作
                HttpUtil util = new HttpUtil();
                MyApplication.session = util.login();
                Message message = handler.obtainMessage();
                message.sendToTarget();
            }

        }.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MyApplication.session == null) {
                dialog.dismiss();
                Toast.makeText(LoginMailsActivity.this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                Intent intent = new Intent(LoginMailsActivity.this, MailsActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(LoginMailsActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
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

    /**
     * 是否记住密码
     */
    private void isRemenberPwd() {
        boolean isRbPwd = sp.getBoolean("psd", false);
        if (isRbPwd) {
            username = sp.getString("account", "");
            userpsd = sp.getString("password", "");
            account.setText(username);
            password.setText(userpsd);
            rememberpsd.setChecked(true);
        }
    }

    private void remenberPwd() {
        boolean isRbPwd = sp.getBoolean("psd", false);
        if (isRbPwd) {
            sp.edit().putBoolean("psd", false).apply();
            rememberpsd.setChecked(false);
        } else {
            sp.edit().putBoolean("psd", true).apply();
            sp.edit().putString("account", account.getText().toString().trim()).apply();
            sp.edit().putString("password", password.getText().toString().trim()).apply();
            rememberpsd.setChecked(true);

        }
    }
}
