package com.jlu.edu.syllabus.adapter;

/**
 * Created by zhengheming on 2016/3/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    public static interface IOnItemSelectListener {
        public void onItemClick(int position);
    }

    ;

    private List<String> list;

    private LayoutInflater mInflater;

    public SpinnerAdapter(Context context, List<String> list) {
        this.list = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void refreshData(List<String> list){
        this.list = list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_syllabus_ua_spiner_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.syllabus_spiner);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextView.setText(list.get(position));

        return convertView;
    }


    public static class ViewHolder {
        public TextView mTextView;
    }

}
