package com.jlu.edu.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.fragment.Fragment_main_four;
import com.jlu.edu.fragment.Fragment_main_one;
import com.jlu.edu.fragment.Fragment_main_three;
import com.jlu.edu.fragment.Fragment_main_two;

import utils.SysActivity;

/**
 * Created by zhengheming on 2015/12/26.
 */
public class MainActivity extends Activity {

    private Fragment_main_one fragment_main_one;
    private Fragment_main_two fragment_main_two;
    private Fragment_main_three fragment_main_three;
    private Fragment_main_four fragment_main_four;
    private RadioButton radio_one;
    private RadioButton radio_two;
    private RadioButton radio_three;
    private RadioButton radio_four;
    private RadioGroup radio_zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysActivity.getInstance().addActivity("MainActivity",MainActivity.this);
        init();
        gragment_init();
        click();
    }

    private void click() {
        radio_zero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_bottom_one:
                        getFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_main_one).commit();

                        break;
                    case R.id.main_bottom_two:
                        getFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_main_two).commit();
                        break;
                    case R.id.main_bottom_three:
                        getFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_main_three).commit();
                        break;
                    case R.id.main_bottom_four:
                        getFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_main_four).commit();
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init() {
        radio_zero = (RadioGroup) findViewById(R.id.main_bottom_zero);
        radio_one = (RadioButton) findViewById(R.id.main_bottom_one);
        radio_two = (RadioButton) findViewById(R.id.main_bottom_two);
        radio_three = (RadioButton) findViewById(R.id.main_bottom_three);
        radio_four = (RadioButton) findViewById(R.id.main_bottom_four);

    }

    private void gragment_init() {
        fragment_main_one = new Fragment_main_one();
        fragment_main_two = new Fragment_main_two();
        fragment_main_three = new Fragment_main_three();
        fragment_main_four = new Fragment_main_four();
        Intent intent = getIntent();
        int temp = intent.getIntExtra("temp", 0);
        if (temp == 0) {
            getFragmentManager().beginTransaction().add(R.id.main_frame, fragment_main_one).commit();
            radio_one.setChecked(true);
        } else if (temp == 1) {
            getFragmentManager().beginTransaction().add(R.id.main_frame, fragment_main_two).commit();
            radio_two.setChecked(true);
        } else if (temp == 2) {
            getFragmentManager().beginTransaction().add(R.id.main_frame, fragment_main_three).commit();
            radio_three.setChecked(true);
        } else if (temp == 3) {
            getFragmentManager().beginTransaction().add(R.id.main_frame, fragment_main_four).commit();
            radio_four.setChecked(true);
        }


    }

    //不再保存Fragment的状态，达到其随着MainActivity一起被回收的效果！
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }
}
