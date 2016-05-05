package com.jlu.edu.personalspace.pedometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import utils.SpUtil;

/**
 * 灵敏度
 * 步长
 * 体重
 * Created by zhengheming on 2016/3/17.
 */
public class PedometerSetting extends Activity implements View.OnClickListener, PedometerDialog.InterfaceDialog {
    private static final String TAG = PedometerSetting.class.getName();
    private TextView sensitivity;
    private TextView step_length;
    private TextView weight;
    private LinearLayout sensitivity_lin;
    private LinearLayout step_length_lin;
    private LinearLayout weight_lin;
    private int step_length_data;
    private int sensitivity_data;
    private int weight_data;

    private TextView back;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometersetting);
        initData();
        init();
        initImpl();
    }

    private void initData() {
        sensitivity_data = (int) new SpUtil("pedometer").send().getFloat("sensitivity", 5);
        step_length_data = new SpUtil("pedometer").send().getInt("step_length", 40);
        weight_data = new SpUtil("pedometer").send().getInt("weight", 60);

    }

    private void init() {

        back = (TextView) findViewById(R.id.personal_pedometer_setting_back);
        sensitivity = (TextView) findViewById(R.id.personal_pedometer_setting_sensitivity);
        step_length = (TextView) findViewById(R.id.personal_pedometer_setting_step_length);
        weight = (TextView) findViewById(R.id.personal_pedometer_setting_weight);
        sensitivity_lin = (LinearLayout) findViewById(R.id.personal_pedometer_setting_lin_sensitivity);
        step_length_lin = (LinearLayout) findViewById(R.id.personal_pedometer_setting_lin_step_length);
        weight_lin = (LinearLayout) findViewById(R.id.personal_pedometer_setting_lin_weight);

    }

    private void initImpl() {
        sensitivity.setText(sensitivity_data + "");
        step_length.setText(step_length_data + "");
        weight.setText(weight_data + "");
        sensitivity_lin.setOnClickListener(this);
        step_length_lin.setOnClickListener(this);
        weight_lin.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        PedometerDialog dialog;
        switch (v.getId()) {
            case R.id.personal_pedometer_setting_lin_sensitivity:
                type = 0;
                dialog = new PedometerDialog(PedometerSetting.this, type);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setInterface(this);
                dialog.show();
                break;
            case R.id.personal_pedometer_setting_lin_step_length:
                type = 1;
                dialog = new PedometerDialog(PedometerSetting.this, type);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setInterface(this);
                dialog.show();
                break;
            case R.id.personal_pedometer_setting_lin_weight:
                type = 2;
                dialog = new PedometerDialog(PedometerSetting.this, type);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setInterface(this);
                dialog.show();
                break;
            case R.id.personal_pedometer_setting_back:
                finish();
                break;
        }

    }


    @Override
    public void getData(int process) {
        Message msg = new Message();
        msg.arg1 = process;
        handler.sendMessage(msg);
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (type == 0) {
                sensitivity.setText("" + msg.arg1);
            } else if (type == 1) {
                step_length.setText("" + msg.arg1);
            } else if (type == 2) {
                weight.setText("" + msg.arg1);

            }
        }
    };
}
