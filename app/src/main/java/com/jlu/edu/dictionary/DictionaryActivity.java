package com.jlu.edu.dictionary;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jlu.edu.csmla.R;

/**
 * Created by zhengheming on 2016/1/31.
 */
public class DictionaryActivity extends Activity {

    private Fragment_dict_one fragment_dict_one;
    private Fragment_dict_two fragment_dict_two;


    private RadioButton radio_one;
    private RadioButton radio_two;
    private RadioGroup radio_zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);
        init();
        fragment_init();
        click();
    }

    private void click() {
        radio_zero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.dict_bottom_one:
                        getFragmentManager().beginTransaction().replace(R.id.dict_frame, fragment_dict_one).commit();

                        break;
                    case R.id.dict_bottom_two:
                        getFragmentManager().beginTransaction().replace(R.id.dict_frame, fragment_dict_two).commit();
                        break;

                    default:
                        Toast.makeText(DictionaryActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init() {
        radio_zero = (RadioGroup) findViewById(R.id.dict_bottom_zero);
        radio_one = (RadioButton) findViewById(R.id.dict_bottom_one);
        radio_two = (RadioButton) findViewById(R.id.dict_bottom_two);

    }

    private void fragment_init() {
        fragment_dict_one = new Fragment_dict_one();
        fragment_dict_two = new Fragment_dict_two();
        getFragmentManager().beginTransaction().add(R.id.dict_frame, fragment_dict_one).commit();
        radio_one.setChecked(true);

    }

    //不再保存Fragment的状态，达到其随着MainActivity一起被回收的效果！
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return false;

    }

}
