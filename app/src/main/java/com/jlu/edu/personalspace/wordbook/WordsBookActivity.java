package com.jlu.edu.personalspace.wordbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.main.MainActivity;

import java.util.List;

import SQLite.MySQLiteImpl;

/**
 * Created by zhengheming on 2016/2/3.
 */
public class WordsBookActivity extends Activity{
    private  static final String TAG=WordsBookActivity.class.getName();
    private ListView listView;
    private List<String> list;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        init();
        inits();
    }



    private void init() {
        list=new MySQLiteImpl().receWords();
        listView= (ListView) findViewById(R.id.words_listview);
        back= (TextView) findViewById(R.id.words_back);
        listView.setAdapter(new MybaseAdapter(this, list));
    }
    private void inits() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(WordsBookActivity.this, WordsDetailsActivity.class);
                intent.putExtra("word",list.get(position));
                startActivity(intent);
                finish();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordsBookActivity.this, MainActivity.class);
                intent.putExtra("temp",2);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(WordsBookActivity.this, MainActivity.class);
            intent.putExtra("temp",3);
            startActivity(intent);
            finish();
            return true;
        }
        return false;

    }
}
