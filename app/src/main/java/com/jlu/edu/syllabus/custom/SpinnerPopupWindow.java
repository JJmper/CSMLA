package com.jlu.edu.syllabus.custom;

/**
 * Created by zhengheming on 2016/3/3.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jlu.edu.csmla.R;
import com.jlu.edu.syllabus.adapter.SpinnerAdapter;

import java.util.List;


/**
 * 自定义SpinerPopWindow类
 *
 * @author gao_chun
 */
public class SpinnerPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private SpinnerAdapter mAdapter;
    private SpinnerAdapter.IOnItemSelectListener mItemSelectListener;


    public SpinnerPopupWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public void setItemListener(SpinnerAdapter.IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }

    public void setAdatper(SpinnerAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_syllabus_spinner, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);


        mListView = (ListView) view.findViewById(R.id.syllabus_spiner_listview);
        mListView.setOnItemClickListener(this);
    }

    public void refreshData(List<String> list) {

        if (mAdapter != null) {
            mAdapter.refreshData(list);

        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
        dismiss();
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(position);
        }
    }

}
