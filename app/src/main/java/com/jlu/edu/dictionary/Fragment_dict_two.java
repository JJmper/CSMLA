package com.jlu.edu.dictionary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jlu.edu.csmla.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import utils.checkType;

/**
 * Created by zhengheming on 2016/1/31.
 */
public class Fragment_dict_two extends Fragment {
    private View view;
    private EditText content;
    private ImageView trans;
    private TextView text;
    private String url;
    private String app = "20160129000009758";
    private String key = "20NXwI4lEXQCvm45nzRH";
    private String word;
    private String from;
    private String to;
    private int random;
    private String sait;
    private boolean isTrue = false;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dict_two, container, false);
        init();
        click();
        return view;
    }

    private void init() {
        mQueue = Volley.newRequestQueue(getActivity());
        content = (EditText) view.findViewById(R.id.dict_two_edit);
        trans = (ImageView) view.findViewById(R.id.dict_two_button);
        text = (TextView) view.findViewById(R.id.dict_two_result);

    }

    private void click() {
        trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
    }

    private void query() {
        word = content.getText().toString().trim();
        if (word.trim().isEmpty()) {
            text.setText("请输入...");
            return;
        }
        if (checkType.checkType_E(word)) {
            isTrue = false;
        } else if (checkType.checkType_Z(word)) {
            isTrue = true;
        } else {
            text.setText("输入格式有误");
            return;
        }
        Random rd = new Random(100);
        random = rd.nextInt();
        sait = MD5Utils.md5(app + word + random + key);
        if (isTrue) {
            from = "zh";
            to = "en";
        } else {
            from = "en";
            to = "zh";
        }
        url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q="
                + word + "&from=" + from + "&to=" + to + "&appid=" + app + "&salt=" + random
                + "&sign=" + sait;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                String result = null;
                try {
                    if (!jsonObject.has("trans_result")) {
                        return;
                    }
                    JSONArray js = jsonObject.getJSONArray("trans_result");
                    result = js.getJSONObject(0).getString("dst");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                text.setText(result);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError.toString());
            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
