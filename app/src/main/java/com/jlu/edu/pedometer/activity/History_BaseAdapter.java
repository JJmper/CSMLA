package com.jlu.edu.pedometer.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlu.edu.csmla.R;
import com.jlu.edu.pedometer.data.bean.PedometerData;

import java.util.List;

import utils.ChangeType;
import utils.SpUtil;
import utils.TimeUtils;


/**
 * Created by zhengheming on 2016/2/22.
 */
public class History_BaseAdapter extends BaseAdapter {
    private List<PedometerData> list;
    private Context context;
    private int step_length = 40;
    private int weight = 60;
    private int calories = 0;
    private int total_step = 0;

    public History_BaseAdapter(Context context, List<PedometerData> list) {
        this.list = list;
        this.context = context;
        step_length = new SpUtil("pedometer").send().getInt("step_length", 40);
        weight = new SpUtil("pedometer").send().getInt("weight", 60);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.pedometer_history_item, null);
            viewHolder.divider = (LinearLayout) convertView.findViewById(R.id.pedometer_history_item_divider);
            viewHolder.time = (TextView) convertView.findViewById(R.id.pedometer_history_item_time);
            viewHolder.step = (TextView) convertView.findViewById(R.id.pedometer_history_item_step);
            viewHolder.calories = (TextView) convertView.findViewById(R.id.pedometer_history_item_calories);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            viewHolder.divider.setVisibility(View.GONE);
        }
        if (list.size() != 0) {
            Log.i("---------", list.size() + "-----" + list.get(position).getTime() + "---" + list.get(position).getStep());
            total_step = ChangeType.change_S_I(list.get(position).getStep());
            viewHolder.step.setText(total_step + "");
            calories=(int) (weight * total_step * step_length * 0.01 * 0.01);
            viewHolder.calories.setText(calories+"");
            String time=list.get(position).getTime();
            viewHolder.time.setText(TimeUtils.formatDate(time));

        }
        return convertView;
    }

    static class ViewHolder {

        LinearLayout divider;
        TextView time;
        TextView step;
        TextView calories;

    }
}
