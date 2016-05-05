package com.jlu.edu.personalspace.feedback;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

import utils.UrlPath;

/**
 * Created by zhengheming on 2016/3/31.
 */
public class FeedBackActivity extends Activity implements View.OnClickListener {
    private TextView wordsText;
    private EditText editText;
    private Button submit;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
        initImpl();
    }


    private void init() {
        wordsText = (TextView) findViewById(R.id.feedback_text_front);
        editText = (EditText) findViewById(R.id.feedback_edit);
        submit = (Button) findViewById(R.id.feedback_submit);
        back = (TextView) findViewById(R.id.feedback_back);
    }

    private void initImpl() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            private String temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(temp)) {
                    String limitSubstring = getLimitSubstring(temp);
                    if (!TextUtils.isEmpty(limitSubstring)) {
                        if (!limitSubstring.equals(temp)) {
                            Toast.makeText(FeedBackActivity.this, "字数已超过限制",
                                    Toast.LENGTH_SHORT).show();
                            editText.setText(limitSubstring);
                            editText.setSelection(limitSubstring.length());
                        }
                    }
                }
            }
        });

    }

    private String getLimitSubstring(String inputStr) {
        int orignLen = inputStr.length();
        int resultLen = 0;
        String temp = null;
        for (int i = 0; i < orignLen; i++) {
            temp = inputStr.substring(i, i + 1);
            try {

                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (resultLen > 500) {
                wordsText.setText("500");
                return inputStr.substring(0, i);
            }
        }
        wordsText.setText(resultLen + "");
        return inputStr;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_back:
                finish();
                break;
            case R.id.feedback_submit:
                String text=editText.getText().toString();
                if(!"".equals(text))
                feedback(text);
                break;
        }
    }

    private void feedback(String text) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String url = UrlPath.addfeedback;
        params.put("text", text);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(FeedBackActivity.this, "反馈成功",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}
