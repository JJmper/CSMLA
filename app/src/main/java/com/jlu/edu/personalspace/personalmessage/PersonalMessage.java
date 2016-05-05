package com.jlu.edu.personalspace.personalmessage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.jlu.edu.csmla.R;
import com.jlu.edu.domain.UserMsg;

import SQLite.MySQLiteImpl;
import utils.SpUtil;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/3/13.
 */
public class PersonalMessage extends Activity {
    private NetworkImageView portrait;
    private TextView name;
    private TextView sex;
    private TextView number;
    private TextView classify;
    private TextView school;
    private TextView back;
    private UserMsg userMsg;
    private RequestQueue queue;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalmessage);
        init();
        initImpl();
        click();


    }


    private void init() {
        queue = Volley.newRequestQueue(PersonalMessage.this);
        final LruCache<String, Bitmap> mImageCache = new LruCache<>(
                20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                mImageCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return mImageCache.get(key);
            }
        };
        mImageLoader = new ImageLoader(queue, imageCache);
        back = (TextView) findViewById(R.id.personal_message_back);
        portrait = (NetworkImageView) findViewById(R.id.personalmessage_portrait);
        name = (TextView) findViewById(R.id.personalmessage_name);
        sex = (TextView) findViewById(R.id.personalmessage_sex);
        number = (TextView) findViewById(R.id.personalmessage_number);
        classify = (TextView) findViewById(R.id.personalmessage_classify);
        school = (TextView) findViewById(R.id.personalmessage_school);

    }

    private void initImpl() {
        userMsg = new MySQLiteImpl().receUserData(new SpUtil("User").send().getString("Usernumber", ""));
        portrait.setImageUrl(UrlPath.picture + userMsg.getUserportrait(), mImageLoader);
        name.setText(userMsg.getUsername());
        sex.setText(userMsg.getUsersex());
        number.setText(userMsg.getUsernumber());
        classify.setText(userMsg.getUserclassify());
        school.setText(userMsg.getUserschool());
    }

    private void click() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
