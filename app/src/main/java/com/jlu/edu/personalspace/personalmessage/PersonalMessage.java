package com.jlu.edu.personalspace.personalmessage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jlu.edu.csmla.R;
import com.jlu.edu.domain.UserMsg;

import SQLite.MySQLiteImpl;
import utils.LodingDialog;
import utils.SpUtil;
import utils.UrlPath;

/**
 * Created by zhengheming on 2016/3/13.
 */
public class PersonalMessage extends Activity {
    private ImageView portrait;
    private TextView name;
    private TextView sex;
    private TextView number;
    private TextView classify;
    private TextView school;
    private TextView back;
    private UserMsg userMsg;
    private LodingDialog dialog;
    private RequestQueue queue;
    private LinearLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalmessage);
        init();
        initImpl();
        click();


    }


    private void init() {
        root= (LinearLayout) findViewById(R.id.root);
        root.setVisibility(View.INVISIBLE);
        dialog = new LodingDialog(PersonalMessage.this);
        back = (TextView) findViewById(R.id.personal_message_back);
        portrait = (ImageView) findViewById(R.id.personalmessage_portrait);
        name = (TextView) findViewById(R.id.personalmessage_name);
        sex = (TextView) findViewById(R.id.personalmessage_sex);
        number = (TextView) findViewById(R.id.personalmessage_number);
        classify = (TextView) findViewById(R.id.personalmessage_classify);
        school = (TextView) findViewById(R.id.personalmessage_school);
        dialog.show();
        queue = Volley.newRequestQueue(PersonalMessage.this);

    }

    private void initImpl() {
        userMsg = new MySQLiteImpl().receUserData(new SpUtil("User").send().getString("Usernumber", ""));
        name.setText(userMsg.getUsername());
        sex.setText(userMsg.getUsersex());
        number.setText(userMsg.getUsernumber());
        classify.setText(userMsg.getUserclassify());
        school.setText(userMsg.getUserschool());
        ImageRequest imgRequest = new ImageRequest(UrlPath.picture+userMsg.getUserportrait(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.i("--Personal--","success");
                portrait.setImageBitmap(bitmap);
                dialog.dismiss();
                root.setVisibility(View.VISIBLE);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("--Personal--","error");
                dialog.dismiss();
                root.setVisibility(View.VISIBLE);
            }
        });
        queue.add(imgRequest);
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
