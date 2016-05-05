package com.jlu.edu.mail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

/**
 * Created by zhengheming on 2016/4/10.
 */
public class MailsActivity extends Activity implements View.OnClickListener {
    private TextView back;
    private LinearLayout box;
    private LinearLayout attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        init();
        intiImpl();
    }



    private void init() {
        back = (TextView) findViewById(R.id.mails_back);
        box = (LinearLayout) findViewById(R.id.mails_box);
        attachment = (LinearLayout) findViewById(R.id.mails_attachment);
    }
    private void intiImpl() {
        back.setOnClickListener(this);
        box.setOnClickListener(this);
        attachment.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mails_back:
                finish();
                break;
            case R.id.mails_box:
                Intent box_intent = new Intent(MailsActivity.this, MailsBoxActivity.class);
                startActivity(box_intent);
                break;
            case R.id.mails_attachment:
                Intent attachment_intent = new Intent(MailsActivity.this, AttachmentDownloadActivity.class);
                startActivity(attachment_intent);
                break;
        }
    }
}
