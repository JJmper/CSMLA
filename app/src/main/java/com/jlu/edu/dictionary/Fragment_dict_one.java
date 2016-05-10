package com.jlu.edu.dictionary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jlu.edu.csmla.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import SQLite.MySQLiteImpl;
import utils.MyApplication;
import utils.checkType;

/**
 *
 *
 *
 * get
 *
 * utf-8
 *
 *
 * Created by zhengheming on 2016/1/31.
 */
public class Fragment_dict_one extends Fragment {
    private View view;
    private EditText editText;
    private ImageButton imageButton;
    private TextView textView_title;
    private TextView textView;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private String content = null;
    private RequestQueue mQueue;
    private String url = "http://fanyi.youdao.com/openapi.do?keyfrom=jlueducsmla&key=334866769&type=data&doctype=json&version=1.1&q=";
    private String query;
    private boolean isTrue = false;
    private String word = null;
    private boolean isEZ = false;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dict_one, container, false);
        mQueue = Volley.newRequestQueue(getActivity());

        init();
        click();
        return view;
    }


    private void init() {
        editText = (EditText) view.findViewById(R.id.dict_one_edit);
        imageButton = (ImageButton) view.findViewById(R.id.dict_one_enter);
        textView_title = (TextView) view.findViewById(R.id.dict_one_result_title);
        textView = (TextView) view.findViewById(R.id.dict_one_result);
        imageView = (ImageView) view.findViewById(R.id.dict_one_save);
        linearLayout = (LinearLayout) view.findViewById(R.id.dict_one_linear_);

    }

    private void click() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = textView_title.getText().toString();
                if ("".equals(word)) return;

                if (!isTrue) {
                    imageView.setImageResource(R.mipmap.btn_favorite_nor);
                    new MySQLiteImpl().addWord(word);
                } else {
                    imageView.setImageResource(R.mipmap.btn_favorite_sel);
                    new MySQLiteImpl().deleteWord(word);
                }


            }
        });
    }

    private void query() {
        content = editText.getText().toString();
        if (content.trim().isEmpty()) {
            linearLayout.setVisibility(View.GONE);
            textView.setText("请输入...");
            return;
        }

        String path = url + content.trim();
        try {
            path= new String(path.getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Toast.makeText(MyApplication.getAppContext(), path, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(path, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                StringBuilder sb = new StringBuilder();
                try {
                    String errorCode = jsonObject.getString("errorCode");
                    switch (errorCode) {
                        case "20":
                            Toast.makeText(MyApplication.getAppContext(), "要翻译的文本过长", Toast.LENGTH_SHORT).show();
                            return;
                        case "30":
                            Toast.makeText(MyApplication.getAppContext(), "无法进行有效的翻译", Toast.LENGTH_SHORT).show();
                            return;
                        case "40":
                            Toast.makeText(MyApplication.getAppContext(), "不支持的语言类型", Toast.LENGTH_SHORT).show();
                            return;
                        case "50":
                            Toast.makeText(MyApplication.getAppContext(), "无效的key", Toast.LENGTH_SHORT).show();
                            return;
                        case "60":
                            Toast.makeText(MyApplication.getAppContext(), "无词典结果" + content, Toast.LENGTH_SHORT).show();
                            return;
                        case "0":
                            break;
                        default:
                            return;
                    }

                    if (checkType.checkType_Z(content)) {
                        isEZ = true;
                    } else if (checkType.checkType_E(content)) {
                        isEZ = false;
                    }

                    query = jsonObject.getString("query");
                    if (jsonObject.has("basic")) {
                        JSONObject basic = jsonObject
                                .getJSONObject("basic");
                        if (basic.has("us-phonetic")) {
                            String us_phonetic = basic.getString("us-phonetic");
                            sb.append("美式音标：[");
                            sb.append(us_phonetic);
                            sb.append("]    ");
                        }
                        if (basic.has("uk-phonetic")) {
                            String uk_phonetic = basic.getString("uk-phonetic");
                            sb.append("英式音标：[");
                            sb.append(uk_phonetic);
                            sb.append("]\n");
                        }
                        sb.append("\n");
                        sb.append("释义:  ");
                        sb.append(jsonObject.getString("translation"));
                        sb.append("\n");
                        sb.append("\n");
                        if (basic.has("explains")) {
                            JSONArray explains = basic.getJSONArray("explains");
                            for (int i = 0; i < explains.length(); i++) {
                                sb.append(explains.getString(i));
                                sb.append("\n");
                            }
                        }
                    }
                    sb.append("网络释义:\n");
                    sb.append("\n");
                    if (jsonObject.has("web")) {
                        JSONArray arr2 = jsonObject.getJSONArray("web");
                        for (int i = 0; i < arr2.length(); i++) {
                            String key = arr2.getJSONObject(i).getString("key");
                            sb.append(key);
                            sb.append(":\n");
                            JSONArray values = arr2.getJSONObject(i).getJSONArray("value");
                            for (int j = 0; j < values.length(); j++) {
                                sb.append(values.getString(j));
                            }
                            sb.append("\n");
                            sb.append("\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isTrue = new MySQLiteImpl().queryWord(content);
                if (isTrue) {
                    imageView.setImageResource(R.mipmap.btn_favorite_nor);
                } else {
                    imageView.setImageResource(R.mipmap.btn_favorite_sel);
                }
                if (isEZ) {
                    imageView.setVisibility(View.GONE);
                }
                linearLayout.setVisibility(View.VISIBLE);
                textView_title.setText(query);
                textView.setText(sb.toString());
                imageButton.setImageResource(R.drawable.btn_normal);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyApplication.getAppContext(), "未知错误", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);

    }


}
