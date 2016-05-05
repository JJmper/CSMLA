package com.jlu.edu.mail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlu.edu.csmla.R;

import java.util.List;

/**
 * Created by zhengheming on 2016/4/11.
 */
public class AttachmentBaseAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;


    public AttachmentBaseAdapter(Context context, List<String> list) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_mailsbox_details_item, null);
            viewHolder.text = (TextView) convertView.findViewById(R.id.mailsbox_details_item_text);
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
