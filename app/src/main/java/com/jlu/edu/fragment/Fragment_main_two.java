package com.jlu.edu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.activity.LoginMailsActivity;
import com.jlu.edu.mail.activity.PublicMailsActivity;

/**
 * Created by zhengheming on 2016/1/10.
 */
public class Fragment_main_two extends Fragment {

    private Button defined_login;
    private Button fast_login;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_two, container, false);
        init();
        initImpl();
        return view;
    }


    private void init() {
        defined_login = (Button) view.findViewById(R.id.main_two_defined_login);
        fast_login = (Button) view.findViewById(R.id.main_two_fast_login);
    }

    private void initImpl() {

        defined_login.setOnClickListener(MyOnClickListener);
        fast_login.setOnClickListener(MyOnClickListener);


    }

    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_two_defined_login:
                    Intent defined_intent=new Intent(getActivity(), LoginMailsActivity.class);
                    getActivity().startActivity(defined_intent);
                    break;
                case R.id.main_two_fast_login:
                    Intent fast_intent=new Intent(getActivity(), PublicMailsActivity.class);
                    getActivity().startActivity(fast_intent);
                    break;
            }
        }
    };
}
