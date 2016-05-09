package com.jlu.edu.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.main.MainActivity;
import com.jlu.edu.register.LoginActivity;

import utils.SpUtil;
import utils.SysActivity;

/**
 * Created by zhengheming on 2015/12/26.
 */
public class SplashActivity extends Activity {
    private boolean flag = false;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        SysActivity.getInstance().addActivity("SplashActivity",SplashActivity.this);
        flag = new SpUtil("first_input").send().getBoolean("first_input", false);
        logoImage = (ImageView) findViewById(R.id.splash);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(3000);
        logoImage.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //区分是否是第一次进入
                //判断flag
                if (!flag) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    protected void onDestroy() {
        super.onDestroy();
        logoImage.setBackground(null);
        System.gc();//通知进行回收
    }
}
