package com.jlu.edu.mail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.mail.bean.Emails;

import java.util.List;

/**
 * Created by zhengheming on 2016/4/22.
 */
public class PublicMailsBaseAdapter extends BaseAdapter {
    private List<Emails> list;
    private Context context;

    public PublicMailsBaseAdapter(Context context, List<Emails> list) {
        this.context = context;
        this.list = list;
    }
   public void dataOnchange(List<Emails> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_publicmails_item, null);
            viewHolder.teacher = (TextView) convertView.findViewById(R.id.publicmails_teacher);
            viewHolder.acount = (TextView) convertView.findViewById(R.id.publicmails_acount);
            viewHolder.number = (TextView) convertView.findViewById(R.id.publicmails_number);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Emails emails = list.get(position);
        viewHolder.teacher.setText(emails.getTeachername());
        viewHolder.acount.setText(emails.getAccount()+ "");
        viewHolder.number.setText(emails.getEmailname());
        return convertView;
    }

    static class ViewHolder {
        TextView teacher;
        TextView acount;
        TextView number;
    }
}
