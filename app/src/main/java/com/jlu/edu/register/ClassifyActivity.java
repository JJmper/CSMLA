package com.jlu.edu.register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;

import java.io.InputStream;

import utils.SpUtil;
import utils.SysActivity;

/**
 * Created by zhengheming on 2015/12/31.
 */
public class ClassifyActivity extends Activity {
    private static final String TAG = ClassifyActivity.class.getName();
    private ImageView student01;
    private ImageView student02;
    private ImageView teacher01;
    private ImageView teacher02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_classify);
        SysActivity.getInstance().addActivity("ClassifyActivity", ClassifyActivity.this);
        init();
        click();
    }
    private void init() {
        student01 = (ImageView) findViewById(R.id.classify_student01);
        student02 = (ImageView) findViewById(R.id.classify_student02);
        teacher01 = (ImageView) findViewById(R.id.classify_teacher01);
        teacher02 = (ImageView) findViewById(R.id.classify_teacher02);
    }

    private void click() {
        student01.setOnClickListener(MyOnClickListener);
        student02.setOnClickListener(MyOnClickListener);
        teacher01.setOnClickListener(MyOnClickListener);
        teacher02.setOnClickListener(MyOnClickListener);
        student01.setImageBitmap(getImage(R.mipmap.student01));
        student02.setImageBitmap(getImage(R.mipmap.student02));
        teacher01.setImageBitmap(getImage(R.mipmap.teacher01));
        teacher02.setImageBitmap(getImage(R.mipmap.teacher02));
    }

    private void ini(String classify, String sex) {
        new SpUtil("User").save().putString("Userclassify", classify).putString("Usersex", sex).commit();
        Intent intent = new Intent(ClassifyActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.classify_student01:
                    ini("student", "boy");
                    break;
                case R.id.classify_student02:
                    ini("student", "girl");
                    break;
                case R.id.classify_teacher01:
                    ini("teacher", "boy");
                    break;
                case R.id.classify_teacher02:
                    ini("teacher", "girl");
                    break;
                default:
                    Toast.makeText(ClassifyActivity.this, "错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Bitmap getImage(int id) {
        InputStream is = this.getResources().openRawResource(id);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        //   options.inSampleSize = 2;   //width，hight设为原来的十分一
        return BitmapFactory.decodeStream(is, null, options);
    }
//回收内存
    protected void onDestroy() {
        super.onDestroy();
        student01.setImageBitmap(null);
        student02.setImageBitmap(null);
        teacher01.setImageBitmap(null);
        teacher02.setImageBitmap(null);
        System.gc();//通知进行回收
    }
}

