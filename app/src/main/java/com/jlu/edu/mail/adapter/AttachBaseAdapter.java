package com.jlu.edu.mail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengheming on 2016/4/25.
 */
public class AttachBaseAdapter extends BaseAdapter {
    private List<Map<String, String>> list;
    private Context context;

    public AttachBaseAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    public void onDataChange(List<Map<String, String>> list) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_attachment_item, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.attachment_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, String> map = list.get(position);
        viewHolder.text.setText(map.get("filename"));
        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
}
