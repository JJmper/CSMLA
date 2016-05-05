package com.jlu.edu.personalspace.upload;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.main.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import utils.CustomDialog;
import utils.SpUtil;
import utils.SysActivity;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/4/21.
 */
public class FinishingActivity extends Activity implements View.OnClickListener {

    private TextView status;
    private TextView status_title;
    private TextView emailname;

    private LinearLayout root;
    private int type = 0;
    private String emailname_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishing);
        getData();
        init();
        initImpl();
    }


    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("emailtype", 0);
        emailname_ = intent.getStringExtra("emailname");
    }

    private void init() {
        SysActivity.getInstance().addActivity("FinishingActivity", this);
        TextView  back = (TextView) findViewById(R.id.finishing_back);
        status = (TextView) findViewById(R.id.finishing_status);
        status_title = (TextView) findViewById(R.id.finishing_status_struction);
        emailname = (TextView) findViewById(R.id.finishing_emailname);
        Button delete = (Button) findViewById(R.id.finishing_delete);
        Button update = (Button) findViewById(R.id.finishing_update);
        root = (LinearLayout) findViewById(R.id.finishing_root);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);

    }

    private void initImpl() {
        if (type == 0) {
            status.setText("公共邮箱已上传");
            status_title.setText("您上传的邮箱是");
            emailname.setText(emailname_);
        } else {
            status.setText("公共邮箱已更新");
            status_title.setText("您最新的邮箱是");
            emailname.setText(emailname_);
        }
        root.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishing_delete:
                CustomDialog.Builder customBuilder = new CustomDialog.Builder(FinishingActivity.this);
                customBuilder.setMessage("确定要删除邮箱吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        delete();
                                    }
                                });
                CustomDialog dialog = customBuilder.create();
                dialog.show();
                break;
            case R.id.finishing_update:
                update();
                break;
            case R.id.finishing_back:
                finish();
                break;
        }
    }

    private void delete() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.deleteEmails + "?number=" + new SpUtil("User").send().getString("Usernumber", "");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("0".equals(res)) {
                    Toast.makeText(FinishingActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                } else {
                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(FinishingActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update() {
        Intent update = new Intent(FinishingActivity.this, UploadActivity.class);
        update.putExtra("type", 1);
        startActivity(update);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent delete = new Intent(FinishingActivity.this, MainActivity.class);
            delete.putExtra("temp", 3);
            startActivity(delete);
            finish();
        }
    };


}
