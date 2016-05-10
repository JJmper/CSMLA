package com.jlu.edu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jlu.edu.csmla.R;
import com.jlu.edu.dictionary.DictionaryActivity;
import com.jlu.edu.interest.InterestActivity;
import com.jlu.edu.pedometer.activity.PedometerActivity;

import utils.SysActivity;

/**
 *
 *
 * Created by zhengheming on 2016/1/10.
 */
public class Fragment_main_three extends Fragment {


    private View view;
    private LinearLayout linearLayout_interest;
    private LinearLayout linearLayout_pedometer;
    private LinearLayout linearLayout_dictionary;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_three, container, false);
        init();
        Click();
        return view;
    }

    private void Click() {
        linearLayout_interest.setOnClickListener(onClickListener);
        linearLayout_pedometer.setOnClickListener(onClickListener);
        linearLayout_dictionary.setOnClickListener(onClickListener);

    }


    private void init() {
        linearLayout_interest = (LinearLayout) view.findViewById(R.id.three_interest);
        linearLayout_pedometer = (LinearLayout) view.findViewById(R.id.three_pedometer);
        linearLayout_dictionary = (LinearLayout) view.findViewById(R.id.three_dictionary);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.three_interest:
                    if(getActivity()!=null) {
                        SysActivity.getInstance().exit("InterestActivity");
                        Intent intent = new Intent(getActivity(), InterestActivity.class);
                        startActivity(intent);

                    }
                    break;
                case R.id.three_pedometer:
                    if(getActivity()!=null) {
                        SysActivity.getInstance().exit("PedometerActivity");
                        Intent intent = new Intent(getActivity(), PedometerActivity.class);
                        startActivity(intent);

                    }
                    break;
                case R.id.three_dictionary:
                    if(getActivity()!=null) {
                        SysActivity.getInstance().exit("DictionaryActivity");
                        Intent intent = new Intent(getActivity(), DictionaryActivity.class);
                        startActivity(intent);
                    }
                    break;

            }
        }
    };

}
