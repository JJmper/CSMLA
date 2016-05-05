package com.jlu.edu.personalspace.wordbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import java.util.List;

/**
 *
 * Created by zhengheming on 2016/2/3.
 */
public class MybaseAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public MybaseAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.words_item, null);
                viewHolder.textView= (TextView) convertView.findViewById(R.id.words_item_text);
            viewHolder.linearLayout= (LinearLayout) convertView.findViewById(R.id.words_item_);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            viewHolder.linearLayout.setVisibility(View.GONE);
        }
         viewHolder.textView.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
      TextView textView;
       LinearLayout linearLayout;
    }
}
