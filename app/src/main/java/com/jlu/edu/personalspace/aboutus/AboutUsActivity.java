package com.jlu.edu.personalspace.aboutus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

/**
 * Created by zhengheming on 2016/4/1.
 */
public class AboutUsActivity extends Activity{
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        back= (TextView) findViewById(R.id.aboutus_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
