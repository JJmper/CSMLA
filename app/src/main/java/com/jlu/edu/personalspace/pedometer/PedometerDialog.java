package com.jlu.edu.personalspace.pedometer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import utils.SpUtil;

/**
 * Created by zhengheming on 2016/3/31.
 */
public class PedometerDialog extends Dialog {


    public PedometerDialog(Context context, int type) {
        super(context, R.style.DialogStyle);
        this.type = type;
    }

    private TextView title;
    private TextView amount;
    private SeekBar process;
    private Button submit;
    private Button cancel;
    private int type = 0;//0 灵敏度 1 步长 2 体重
    private int step_length;
    private int weight;
    private int sensitivity;
    private InterfaceDialog interfaceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedometer_setting_dialog);
        initData();
        init();
        initImpl();


    }

    public void setInterface(InterfaceDialog interfaceDialog) {
        this.interfaceDialog = interfaceDialog;
    }

    private void initData() {
        sensitivity = (int)new SpUtil("pedometer").send().getFloat("sensitivity", 5);
        step_length = new SpUtil("pedometer").send().getInt("step_length", 40);
        weight = new SpUtil("pedometer").send().getInt("weight", 60);
    }

    private void init() {
        title = (TextView) findViewById(R.id.pedomenter_setting_title);
        amount = (TextView) findViewById(R.id.pedomenter_setting_amount);
        process = (SeekBar) findViewById(R.id.pedometer_setting_progress);
        submit = (Button) findViewById(R.id.pedometer_setting_submit);
        cancel = (Button) findViewById(R.id.pedometer_setting_cancel);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void initImpl() {

        if (type == 0) {
            title.setText("灵敏度:");
            amount.setText(sensitivity + "");
            process.setMax(10);
            process.setProgress((int) sensitivity);
        } else if (type == 1) {
            title.setText("步长(cm):");
            amount.setText(step_length + "");
            process.setMax(100);
            process.setProgress(step_length);
        } else if (type == 2) {
            title.setText("体重(kg):");
            amount.setText(weight + "");
            process.setMax(100);
            process.setProgress(weight);
        }

        process.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amount.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = process.getProgress();
                if (type == 0) {
                    new SpUtil("pedometer").save().putFloat("sensitivity", temp).commit();
                } else if (type == 1) {
                    new SpUtil("pedometer").save().putInt("step_length", temp).commit();
                } else if (type == 2) {
                    new SpUtil("pedometer").save().putInt("weight", temp).commit();
                }
             interfaceDialog.getData(temp);
                dismiss();
            }
        });

    }


    public interface InterfaceDialog {
        public void getData(int process);
    }
}
