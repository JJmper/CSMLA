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
import com.jlu.edu.mail.adapter.MyBaseAdapter;
import com.jlu.edu.mail.bean.Email;
import com.jlu.edu.mail.bean.MailHelper;
import com.jlu.edu.mail.bean.MailReceiver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import utils.LodingDialog;
import utils.MyApplication;

/**
 * Created by zhengheming on 2016/4/10.
 */
public class MailsBoxActivity extends Activity {
    private static final String TAG = MailsBoxActivity.class.getName();
    private ListView listView;
    private TextView back;
    private List<Email> emails = new ArrayList<>();
    private ArrayList<ArrayList<InputStream>> attachmentsInputStreamsList = new ArrayList<>();
    private List<MailReceiver> mailMessages;
    private LodingDialog dialog;
    private MyBaseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailsbox);
        init();
        initImpl();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.mailsbox_listview);
        back = (TextView) findViewById(R.id.mailsbox_back);
        adapter = new MyBaseAdapter(this, emails);
        listView.setAdapter(adapter);
    }

    private void initImpl() {
        dialog = new LodingDialog(this);
        dialog.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MyApplication) getApplication()).setAttachmentsInputStreams(attachmentsInputStreamsList.get(position));
                Intent intent = new Intent(MailsBoxActivity.this, MailsBoxDetailsActivity.class).putExtra("EMAIL", emails.get(position));
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mailMessages = MailHelper.getInstance().getAllMail();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
                getAllMails(mailMessages);
                handler.sendEmptyMessage(2);
            }
        }).start();

    }


    private void getAllMails(List<MailReceiver> mails) {
        for (MailReceiver mailReceiver : mails) {
            Email email = new Email();
            try {
                email.setRecepients(mailReceiver.getMailAddress("to"));
                email.setAddressser(mailReceiver.getFrom());
                email.setTime(mailReceiver.getSentData());
                email.setSubject(mailReceiver.getSubject());
                email.setContent(mailReceiver.getMailContent());
                email.setIsHtml(mailReceiver.isHtml());
                email.setCharset(mailReceiver.getCharset());
                attachmentsInputStreamsList.add(0, mailReceiver.getAttachmentsInputStreams());
                email.setAttach(mailReceiver.getAttachments());
                emails.add(0, email);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    dialog.dismiss();
                    Toast.makeText(MailsBoxActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                    finish();
                case 2:
                    dialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }

    };

}
