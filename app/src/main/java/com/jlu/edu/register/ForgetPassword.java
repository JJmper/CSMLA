package com.jlu.edu.register;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.lang.ref.WeakReference;

import utils.Net;
import utils.PatternAuth;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/1/1.
 */
public class ForgetPassword extends Activity{
    private static final String TAG=ForgetPassword.class.getName();
    private EditText number;
    private EditText email;
    private Button send;
    private String send_number;
    private String send_email;
    private final MyHandler mHandler = new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_forget);
        init();
        click();
    }



    private void init() {
        number= (EditText) findViewById(R.id.forget_number);
        email= (EditText) findViewById(R.id.forget_email);
        send= (Button) findViewById(R.id.forget_send);
    }
    private void click() {

         send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!new Net().isNetworkAvailable(ForgetPassword.this)){
                     Toast.makeText(ForgetPassword.this,"无网络连接",Toast.LENGTH_SHORT).show();
                     return;
                 }
                  send_number=number.getText().toString().trim();
                  send_email=email.getText().toString().trim();
                 if(PatternAuth.getAuth(send_number, "[a-zA-Z0-9_]{3,10}")){
                     if(PatternAuth.email(send_email)){
                         send_email();
                     }else{
                        Toast.makeText(ForgetPassword.this,"邮箱格式错误",Toast.LENGTH_SHORT).show();
                     }
                 }else{
                     Toast.makeText(ForgetPassword.this,"账号格式错误",Toast.LENGTH_SHORT).show();
                 }
             }
         });


    }
    private void send_email() {
        AsyncHttpClient client = new AsyncHttpClient();
        String path = UrlPath.ForgetPassword;
        path = path + "?" + "Usernumber=" + send_number + "&Useremail=" + send_email;
        client.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("1".equals(res)) {
                    mHandler.sendEmptyMessage(0);
                    Toast.makeText(ForgetPassword.this, "发送成功", Toast.LENGTH_SHORT).show();
                } else if ("0".equals(res)) {
                    Toast.makeText(ForgetPassword.this, "用户名或邮箱错误", Toast.LENGTH_SHORT).show();
                } else if ("-1".equals(res)) {
                    Toast.makeText(ForgetPassword.this, "未知错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgetPassword.this, "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(ForgetPassword.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static class MyHandler extends Handler {
        private final WeakReference<ForgetPassword> mActivity;

        public MyHandler(ForgetPassword activity) {
            mActivity = new WeakReference<ForgetPassword>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ForgetPassword activity = mActivity.get();
            if (activity != null) {
                {
                      activity.finish();

                }
            }
        }
    }

}
