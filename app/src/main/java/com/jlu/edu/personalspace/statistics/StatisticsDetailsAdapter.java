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
public class StatisticsDetailsAdapter extends BaseAdapter {

    private Context context;
    private List<Statistics_record> list;

    public StatisticsDetailsAdapter(Context context, List<Statistics_record> list) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_statistics_details_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.statistics_details_name);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.statistics_details_amount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Statistics_record record=list.get(position);
        viewHolder.name.setText(record.getStudent());
        viewHolder.amount.setText(record.getCount()+"");
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView amount;
    }


}
