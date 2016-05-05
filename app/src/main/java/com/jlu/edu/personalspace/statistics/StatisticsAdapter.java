package com.jlu.edu.personalspace.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import java.util.List;

/**
 * Created by zhengheming on 2016/4/1.
 */
public class StatisticsAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public StatisticsAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void OnDataChange(List<String> list) {
        this.list = list;
        this.notifyDataSetChanged();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_statistics_item, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.activity_statistics_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
         viewHolder.text.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
}
