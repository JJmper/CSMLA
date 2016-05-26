package com.jlu.edu.syllabus.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.syllabus.adapter.SpinnerAdapter;
import com.jlu.edu.syllabus.custom.SpinnerPopupWindow;
import com.jlu.edu.syllabus.domain.Course;
import com.jlu.edu.syllabus.utils.SyllabusTimeChange;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import SQLite.MySQLiteImpl;
import utils.ChangeType;
import utils.UrlPath;

/**
 * 更新课程表
 * 课表名称       下拉菜单
 * 上课位置       下拉菜单
 * 教师名称       下拉菜单
 * 课表星期       下拉菜单
 * 课程开始       下拉菜单
 * 课程结束       下拉菜单
 * Created by zhengheming on 2016/3/1.
 */
public class UpdateOrAddSyllabusActivity extends Activity implements PopupWindow.OnDismissListener, SpinnerAdapter.IOnItemSelectListener {
    private static final String TAG = UpdateOrAddSyllabusActivity.class.getName();
    private TextView back;
    private TextView title;
    private TextView submit;
    private LinearLayout linearLayout_name;
    private LinearLayout linearLayout_teacher;
    private LinearLayout linearLayout_place;
    private LinearLayout linearLayout_week;
    private LinearLayout linearLayout_start;
    private LinearLayout linearLayout_step;
    private TextView name;
    private ImageView name_arrow;
    private TextView teacher;
    private ImageView teacher_arrow;
    private TextView place;
    private ImageView place_arrow;
    private TextView week;
    private ImageView week_arrow;
    private TextView start;
    private ImageView start_arrow;
    private TextView step;
    private ImageView step_arrow;
    private int type = 0;//type--->0 (添加课程) 1 (修改课程)
    private List<String> list;
    private Drawable top;
    private Drawable bottom;
    private SpinnerPopupWindow spinnerPopupWindow;
    private SpinnerAdapter mAdapter;
    private TextView temp;
    private int start_temp = -1;
    private Course front = null;
    private Course last = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus_ua);
        GetData();
        init();
        initImpl();

    }

    private void GetData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("TYPE", 0);
        if (type == 1) {
            String temp = intent.getStringExtra("text");
            String[] arr = temp.split("\\n");
            String name_ = arr[0];
            String place_ = arr[1];
            String teacher_ = arr[2];
            int week_ = intent.getIntExtra("week", 0);
            int start_ = intent.getIntExtra("start", 0);
            int step_ = intent.getIntExtra("step", 0);
            front = new Course(name_, teacher_, place_, week_, start_, step_);

        }


    }


    private void init() {
        back = (TextView) findViewById(R.id.syllabus_ua_back);
        title = (TextView) findViewById(R.id.syllabus_ua_title);
        submit = (TextView) findViewById(R.id.syllabus_ua_submit);
        name = (TextView) findViewById(R.id.syllabus_name);
        name_arrow = (ImageView) findViewById(R.id.syllabus_name_arrow);
        teacher = (TextView) findViewById(R.id.syllabus_teacher);
        teacher_arrow = (ImageView) findViewById(R.id.syllabus_teacher_arrow);
        place = (TextView) findViewById(R.id.syllabus_place);
        place_arrow = (ImageView) findViewById(R.id.syllabus_place_arrow);
        week = (TextView) findViewById(R.id.syllabus_week);
        week_arrow = (ImageView) findViewById(R.id.syllabus_week_arrow);
        start = (TextView) findViewById(R.id.syllabus_start);
        start_arrow = (ImageView) findViewById(R.id.syllabus_start_arrow);
        step = (TextView) findViewById(R.id.syllabus_step);
        step_arrow = (ImageView) findViewById(R.id.syllabus_step_arrow);
        linearLayout_name = (LinearLayout) findViewById(R.id.syllabus_linear_name);
        linearLayout_teacher = (LinearLayout) findViewById(R.id.syllabus_linear_teacher);
        linearLayout_place = (LinearLayout) findViewById(R.id.syllabus_linear_place);
        linearLayout_week = (LinearLayout) findViewById(R.id.syllabus_linear_week);
        linearLayout_start = (LinearLayout) findViewById(R.id.syllabus_linear_start);
        linearLayout_step = (LinearLayout) findViewById(R.id.syllabus_linear_step);
        linearLayout_name.setOnClickListener(MyOnClickListener);
        linearLayout_teacher.setOnClickListener(MyOnClickListener);
        linearLayout_place.setOnClickListener(MyOnClickListener);
        linearLayout_week.setOnClickListener(MyOnClickListener);
        linearLayout_start.setOnClickListener(MyOnClickListener);
        linearLayout_step.setOnClickListener(MyOnClickListener);
        linearLayout_step.setClickable(false);
        back.setOnClickListener(MyOnClickListener);
        submit.setOnClickListener(MyOnClickListener);
        bottom = getResources().getDrawable(R.mipmap.syllabus_bottom);
        top = getResources().getDrawable(R.mipmap.syllabus_top);


    }

    private void initPopup(List<String> list) {
        if (mAdapter != null) {
            spinnerPopupWindow.refreshData(list);
        } else {
            mAdapter = new SpinnerAdapter(UpdateOrAddSyllabusActivity.this, list);
        }

        spinnerPopupWindow = new SpinnerPopupWindow(UpdateOrAddSyllabusActivity.this);
        spinnerPopupWindow.setAdatper(mAdapter);
        spinnerPopupWindow.setItemListener(this);
        spinnerPopupWindow.setOutsideTouchable(true);
        spinnerPopupWindow.setOnDismissListener(this);

    }

    private void showSpinWindow(List<String> list, TextView textView) {
        initPopup(list);
        spinnerPopupWindow.setWidth(textView.getWidth());
        spinnerPopupWindow.showAsDropDown(textView);
    }

    private void initImpl() {
        if (type == 0) {
            title.setText("添加课程");
        } else {
            title.setText("修改课程");
            name.setText(front.getName());
            teacher.setText(front.getTeacher());
            place.setText(front.getPlace());
            week.setText(front.getWeek() + "");
            start.setText(SyllabusTimeChange.Change(front.getStart()));
            step.setText(SyllabusTimeChange.Change(front.getStart(), front.getStep()));
        }
    }


    //将数组转成集合
    private List<String> fillData(String[] arr) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }

    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.syllabus_linear_name:
                    name_arrow.setBackground(bottom);
                    list = fillData(getResources().getStringArray(R.array.syllabus_name));
                    Collections.sort(list);
                    showSpinWindow(list, name);
                    temp = name;
                    break;
                case R.id.syllabus_linear_teacher:
                    teacher_arrow.setBackground(bottom);
                    list = fillData(getResources().getStringArray(R.array.syllabus_teacher));
                    Collections.sort(list);
                    showSpinWindow(list, teacher);
                    temp = teacher;
                    break;
                case R.id.syllabus_linear_place:
                    place_arrow.setBackground(bottom);
                    list = fillData(getResources().getStringArray(R.array.syllabus_place));
                    showSpinWindow(list, place);
                    temp = place;
                    break;
                case R.id.syllabus_linear_week:
                    week_arrow.setBackground(bottom);
                    list = fillData(getResources().getStringArray(R.array.syllabus_week));
                    showSpinWindow(list, week);
                    temp = week;
                    break;
                case R.id.syllabus_linear_start:
                    start_arrow.setBackground(bottom);
                    list = fillData(getResources().getStringArray(R.array.syllabus_start));
                    showSpinWindow(list, start);
                    temp = start;
                    break;
                case R.id.syllabus_linear_step:
                    step_arrow.setBackground(bottom);
                    list = fillData(getResources().getStringArray(R.array.syllabus_step_1 + start_temp));
                    showSpinWindow(list, step);
                    temp = step;
                    break;
                case R.id.syllabus_ua_back:
                    Intent intent = new Intent(UpdateOrAddSyllabusActivity.this, SyllabusActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.syllabus_ua_submit:

                    //两个任务
                    //一：如果添加课程    直接添加课程到服务器和本地数据库
                    last = ChangeCourse();
                    if (last == null) {
                        return;
                    }
                    AddDataToService(last);
                    //二：如果是更新课程   先添加课程到服务器，和本地数据库，后删除之前数据
                    break;
            }
        }
    };

    private Course ChangeCourse() {
        //判断是否存在空项（怎么判断：）
        String _name = name.getText().toString();
        String _teacher = teacher.getText().toString();
        String _place = place.getText().toString();
        String _week = week.getText().toString();
        String _start = start.getText().toString();
        String _step = step.getText().toString();
        String res = null;
        switch (1) {
            case 1:
                if ("".equals(_name)) {
                    res = "未选择课程名称";
                    break;
                }
            case 2:
                if ("".equals(_teacher)) {
                    res = "未选择课程教师";
                    break;
                }
            case 3:
                if ("".equals(_place)) {
                    res = "未选择课程地点";
                    break;
                }
            case 4:
                if ("".equals(_week)) {
                    res = "未选择课程时间（星期）";
                    break;
                }
            case 5:
                if ("".equals(_start)) {
                    res = "未选择课程开始时间";
                    break;
                }
            case 6:
                if ("".equals(_step)) {
                    res = "未选择课程结束时间";
                    break;
                }
            default:
                break;

        }
        if (res != null) {
            Toast.makeText(UpdateOrAddSyllabusActivity.this, res, Toast.LENGTH_SHORT).show();
            return null;
        } else {
            //判断 怎么保证与现有的数据不重合，时间上不重复
            //根据1，2，3，4，5，6，7，8，9，10，11，12十二节课的不重复性，判断
            //提前将已有课程的时间放进集合中，再进行判断。
            last = new Course(_name, _teacher, _place, ChangeType.change_S_I(_week), SyllabusTimeChange.Change(_start), SyllabusTimeChange.Change(_start, _step));
            boolean flag = CheckRepeat(ChangeType.change_S_I(_week));
            if (!flag) {
                Toast.makeText(UpdateOrAddSyllabusActivity.this, "课程时间冲突，请重新设置", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return last;
    }

    private boolean CheckRepeat(int week) {
        boolean flag = true;
        Set<Integer> set = new HashSet<>();
        List<Course> list = new MySQLiteImpl().querySyllabus(week);
        for (Course course : list) {
            int start = course.getStart();
            int step = course.getStep();
            if ((front != null) && (start != front.getStart())) {
                for (int i = 0; i < step; i++) {
                    set.add(start + i);
                }
            }
        }
        int last_start = last.getStart();
        int last_step = last.getStep();
        for (int j = 0; j < last_step; j++) {
            boolean temp_flag = set.add(last_start + j);
            if (!temp_flag) {
                flag = false;
            }
        }
        return flag;
    }

    private void AddDataToService(final Course course) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.AddSyllabusServlet;
        RequestParams params = new RequestParams();
        params.put("usernumber", course.getUsernumber());
        params.put("name", course.getName());
        params.put("teacher", course.getTeacher());
        params.put("place", course.getPlace());
        params.put("week", course.getWeek());
        params.put("start", course.getStart());
        params.put("step", course.getStep());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("1".equals(res)) {
                    boolean flag = new MySQLiteImpl().addSyllabus(course);
                    if (flag) {
                        if (type == 1) {
                            DeleteDataToService(front);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    } else {
                        Toast.makeText(UpdateOrAddSyllabusActivity.this, "添加本地失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateOrAddSyllabusActivity.this, "添加网络失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(UpdateOrAddSyllabusActivity.this, "添加网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void DeleteDataToService(final Course course) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.DeleteSyllabusServlet;
        RequestParams params = new RequestParams();
        params.put("usernumber", course.getUsernumber());
        params.put("name", course.getName());
        params.put("teacher", course.getTeacher());
        params.put("place", course.getPlace());
        params.put("week", course.getWeek());
        params.put("start", course.getStart());
        params.put("step", course.getStep());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("1".equals(res)) {
                    boolean flag = new MySQLiteImpl().deleteSyllabus(course);
                    if (flag) {
                        handler.sendEmptyMessage(0);
                    } else {
                        Toast.makeText(UpdateOrAddSyllabusActivity.this, "删除数据本地失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateOrAddSyllabusActivity.this, "删除数据网络失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(UpdateOrAddSyllabusActivity.this, "删除数据网络失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        if (temp.getId() == R.id.syllabus_start) {
            linearLayout_step.setClickable(true);
            start_temp = position;
        }
        temp.setText(list.get(position));
    }

    @Override
    public void onDismiss() {
        name_arrow.setBackground(top);
        teacher_arrow.setBackground(top);
        place_arrow.setBackground(top);
        week_arrow.setBackground(top);
        start_arrow.setBackground(top);
        step_arrow.setBackground(top);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
            Intent sm = new Intent(UpdateOrAddSyllabusActivity.this, SyllabusActivity.class);
            startActivity(sm);
            finish();
        }
    };
}
