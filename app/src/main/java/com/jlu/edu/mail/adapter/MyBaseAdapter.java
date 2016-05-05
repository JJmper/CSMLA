package com.jlu.edu.mail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.bean.Email;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengheming on 2016/4/10.
 */
public class MyBaseAdapter extends BaseAdapter {
    private Context context;
    private List<Email> mailslist = new ArrayList<Email>();

    public MyBaseAdapter(Context context, List<Email> mailslist) {
        this.context = context;
        this.mailslist = mailslist;
    }

    @Override
    public int getCount() {
        return mailslist.size();
    }

    @Override
    public Object getItem(int position) {
        if (position > mailslist.size() - 1) {
            return null;
        }
        return mailslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_mailsbox_item, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.mailsbox_item_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.mailsbox_item_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.mailsbox_item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Email email = mailslist.get(position);
        viewHolder.time.setText(email.getTime());
        viewHolder.title.setText(email.getAddressser());
        viewHolder.content.setText(email.getSubject());
        return convertView;
    }

    static class ViewHolder {
        TextView time;
        TextView title;
        TextView content;
    }
}
