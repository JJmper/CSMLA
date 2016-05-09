package com.jlu.edu.register;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.main.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.ref.WeakReference;

import utils.DesUtils;
import utils.Net;
import utils.SpUtil;
import utils.SysActivity;
import utils.UrlPath;

/**
 *
 * Created by zhengheming on 2015/12/27.
 */
public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getName();
    private EditText number;
    private EditText password;
    private TextView forget_password;
    private CheckBox checkBox;
    private Button login_login;
    private Button login_register;
    private static String edit_number;
    private static String edit_password;
    private final MyHandler mHandler = new MyHandler(this);
    private DesUtils des = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SysActivity.getInstance().addActivity("LoginActivity", LoginActivity.this);
        init();
        click();
    }

    private void init() {
        try {
            des = new DesUtils("csmla");//自定义密钥
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences sp = new SpUtil("User").send();
        String usernumber = sp.getString("Usernumber", "");
        String userpassword = sp.getString("Userpassword", "");
        number = (EditText) findViewById(R.id.login_number);
        String classify = new SpUtil("User").send().getString("Userclassify", "student");
        String cla;
        if ("student".equals(classify)) {
            cla = "学号";
        } else {
            cla = "教学号";
        }
        number.setHint(cla);
        number.setText(usernumber);
        password = (EditText) findViewById(R.id.login_password);
        password.setText(userpassword);
        forget_password = (TextView) findViewById(R.id.login_forget);
        checkBox = (CheckBox) findViewById(R.id.login_remember_checkbox);
        if (!"".equals(userpassword)) {
            checkBox.setChecked(true);
        }
        login_register = (Button) findViewById(R.id.login_register);
        login_login = (Button) findViewById(R.id.login_login);
    }

    private void click() {
        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Net.isNetworkAvailable(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                edit_number = number.getText().toString().trim();
                edit_password = password.getText().toString().trim();
                if (edit_number != null && !"".equals(edit_number) && !"".equals(edit_password))
                    Login_get();
            }
        });
        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register_school.class);
                startActivity(intent);
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void Login_get() {
        AsyncHttpClient client = new AsyncHttpClient();
        String path = UrlPath.login;
        try {
            path = path + "?" + "Usernumber=" + edit_number + "&Userpassword=" + des.encrypt(edit_password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if (res.length() == 32) {
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                } else if ("0".equals(res)) {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                } else if ("-1".equals(res)) {
                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        public MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity != null) {
                {
                    if (msg.what == 1) {
                        new SpUtil("User").save().putString("UserToken", msg.getData().getString("res")).putString("Usernumber", edit_number).putString("Userpassword", edit_password).commit();
                    }
                    if (activity.checkBox.isChecked()) {
                        new SpUtil("User").save().putString("Usernumber", edit_number).putString("Userpassword", edit_password).commit();
                    } else {
                        new SpUtil("User").save().putString("Usernumber", edit_number).putString("Userpassword", "").commit();
                    }
                    Toast.makeText(activity, "登陆成功",
                            Toast.LENGTH_LONG).show();

                    new SpUtil("first_input").save().putBoolean("first_input", true).commit();
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    activity.finish();


                }
            }
        }
    }

}
