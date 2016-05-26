package com.jlu.edu.interest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.jlu.edu.domain.Interest_comment;
import com.jlu.edu.domain.Interest_data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import HttpUtils.User_http;
import SQLite.MySQLiteImpl;
import utils.Comment_PopupWindow;
import utils.LodingDialog;
import utils.Net;
import utils.SpUtil;
import utils.SysActivity;
import utils.UrlPath;

/**
 * 问题一：上拉刷新问题：在做完评论列表不改变位置后，上拉刷新“失灵”了，
 * 经过多次查看和考虑，发现自定义的listview里已实现滚动接口，在此活动中，
 * 再次实现接口，并覆盖之前接口，导致上拉刷新功能缺失解决办法，将自定义布
 * 局中实现滚动接口，并定义两个静态变量，供调用
 * 问题二：下拉加载问题：
 * <p/>
 * <p/>
 * 问题三：数据库 操作  只保留五条最新数据（五条信息及若干评论信息） ，在没有网络时调用。
 * <p/>
 * 在有网络时 先清空数据库 ，再插入最新数据（五条）
 * <p/>
 * <p/>
 * Created by zhengheming on 2016/1/11.
 */
public class InterestActivity extends Activity implements Interest_ListView.ILoadListener, Interest_ListView.IRefleshListener, Interest_Adapter.Comment {
    private static final String TAG = InterestActivity.class.getName();
    private ImageView send;
    private TextView back;
    private Interest_ListView listView;
    private List<Interest_data> list;
    private Interest_Adapter adapter;
    private Comment_PopupWindow menuWindow;
    private String passive;
    private String interestid;
    private int position;
    private MySQLiteImpl mySQLite;
    private int onloadtemp = 5;//加载计数
    private LodingDialog dialog;
    private boolean isAll = false;
    private boolean isreply=false;
    private String active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        SysActivity.getInstance().addActivity("InterestActivity", InterestActivity.this);
        init();
        initImpl();
        click();

    }

    private void init() {
        active=new SpUtil("User").send().getString("Usernumber", "");
        list = new ArrayList<>();
        mySQLite = new MySQLiteImpl();
        dialog = new LodingDialog(InterestActivity.this);
        send = (ImageView) findViewById(R.id.interest_send);
        back = (TextView) findViewById(R.id.interest_back);
        listView = (Interest_ListView) findViewById(R.id.interest_list);
        listView.setInterface((Interest_ListView.ILoadListener) this);
        listView.setInterface((Interest_ListView.IRefleshListener) this);
        adapter = new Interest_Adapter(this, list);
        adapter.SetOnCommentListener(this);
        listView.setAdapter(adapter);

    }

    private void initImpl() {
        if (Net.isNetworkAvailable(InterestActivity.this)) {
            dialog.show();
            ReceInterest(0, 2);
        } else {
            list = mySQLite.receInterestData(0, 5);
        }
    }

    private void click() {

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InterestActivity.this, SendMessageActivity.class);
                startActivity(intent);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onReflesh() {
        if (Net.isNetworkAvailable(this)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(5);
                }
            }, 1500);

        } else {
            Toast.makeText(this, "暂无网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoad() {
        if (Net.isNetworkAvailable(this)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAll) {
                        handler.sendEmptyMessage(7);
                    } else {
                        handler.sendEmptyMessage(6);
                    }

                }
            }, 1500);
        } else {
            Toast.makeText(this, "暂无网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onComment(String interestid, String passive, boolean flag, int position) {
        this.passive = passive;
        this.interestid = interestid;
        this.position = position;
        this.isreply=flag;
        if(!active.equals(passive)){
            menuWindow = new Comment_PopupWindow(InterestActivity.this,
                    itemsOnClick, flag, passive);
            menuWindow.showAtLocation(InterestActivity.this.findViewById(R.id.interest_),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }


    }
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popup_commit:
                    String text = menuWindow.getEditText().getText().toString();
                    if (!"".equals(text)) {
                        if(isreply){
                            Comment_http(interestid, active, passive, text);
                        }else{
                            Comment_http(interestid, active, "NO", text);
                        }
                        menuWindow.dismiss();
                    }
                    break;
            }
        }
    };

    private void Comment_http(String interestid, String activenumber, String passivenumber, String content) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlPath.AddInterestComment;
        RequestParams params = new RequestParams();
        params.put("Interestid", interestid);
        params.put("Interest_comment_active", activenumber);
        params.put("Interest_comment_passive", passivenumber);
        params.put("Interest_comment_content", content);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("-1".equals(res)) {
                    Toast.makeText(InterestActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                } else {
                    //更新list数据   添加新评论
                    try {
                        update(res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(InterestActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    adapter.onDateChange(list);
                    listView.setAdapter(adapter);
                    listView.setSelectionFromTop(Interest_ListView.position, Interest_ListView.scrolledY);
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(InterestActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //刷新成功
                    adapter.onDateChange(list);
                    if (list.size() == 0) {
                        Toast.makeText(InterestActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InterestActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                    onloadtemp = 5;
                    listView.refleshComplete();
                    break;
                case 1:
                    Toast.makeText(InterestActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                    onloadtemp = onloadtemp + 5;
                    adapter.onDateChange(list);
                    listView.loadComplete();
                    break;
                case 2:
                    //第一次进入获取数据
                    if (list.size() == 0) {
                        Toast.makeText(InterestActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                    onloadtemp = 5;
                    adapter.onDateChange(list);
                    dialog.dismiss();
                    break;
                case 3:
                    Toast.makeText(InterestActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(InterestActivity.this, "服务器访问失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    //刷新
                    ReceInterest(0, 0);
                    break;
                case 6:
                    //加载
                    ReceInterest(onloadtemp, 1);
                    break;
                case 7:
                    adapter.onDateChange(list);
                    if (list.size() == 0) {
                        Toast.makeText(InterestActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InterestActivity.this, "已加载全部", Toast.LENGTH_SHORT).show();
                    }
                    listView.loadComplete();
                    break;
            }
        }

    };

    private void ReceInterest(final int start, final int type) {
        AsyncHttpClient Client = new AsyncHttpClient();
        String url = UrlPath.ReceInterestData + "?start=" + start;
        Client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int s, Header[] headers, byte[] bytes) {

                String res = new String(bytes);

                try {
                    if (type == 0 || type == 2) {
                        list.clear();
                    }

                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("interest");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Interest_data interest_data = new Interest_data();
                        JSONObject item = jsonArray.getJSONObject(i);
                        String interestcontent = item.getString("interestcontent");
                        int interestid = item.getInt("interestid");
                        String interestimage = item.getString("interestimage");
                        String interestname = item.getString("interestname");
                        String interestnumber = item.getString("interestnumber");
                        String interestportrait = item.getString("interestportrait");
                        String interestsex = item.getString("interestsex");
                        long interesttime = item.getLong("interesttime");
                        interest_data.setInterestid(interestid);
                        interest_data.setInterestcontent(interestcontent);
                        interest_data.setInterestimage(interestimage);
                        interest_data.setInterestname(interestname);
                        interest_data.setInterestnumber(interestnumber);
                        interest_data.setInterestportrait(interestportrait);
                        interest_data.setInterestsex(interestsex);
                        interest_data.setInteresttime(interesttime);
                        JSONArray json = item.getJSONArray("list");
                        List<Interest_comment> sublist = new ArrayList<>();
                        for (int j = 0; j < json.length(); j++) {
                            Interest_comment interest_comment = new Interest_comment();
                            JSONObject it = json.getJSONObject(j);
                            int interest_commentid = it.getInt("interest_commentid");
                            String interest_comment_active = it.getString("interest_comment_active");
                            String interest_comment_content = it.getString("interest_comment_content");
                            String interest_comment_passive = it.getString("interest_comment_passive");
                            String interest_active_name = it.getString("interest_active_name");
                            String interest_passive_name = it.getString("interest_passive_name");
                            if (mySQLite.receUserData(interest_comment_active).getUsername() == null) {
                                new User_http(interest_comment_active).AsyncHttpService();
                            }
                            if (mySQLite.receUserData(interest_comment_passive).getUsername() == null) {
                                new User_http(interest_comment_passive).AsyncHttpService();
                            }
                            interest_comment.setInterest_commentid(interest_commentid);
                            interest_comment.setInterestid(interestid);
                            interest_comment.setInterest_comment_active(interest_comment_active);
                            interest_comment.setInterest_comment_passive(interest_comment_passive);
                            interest_comment.setInterest_comment_content(interest_comment_content);
                            interest_comment.setInterest_active_name(interest_active_name);
                            interest_comment.setInterest_passive_name(interest_passive_name);
                            sublist.add(interest_comment);
                            if (start == 0) {
                                mySQLite.addInterestComment(interest_commentid + "", interestid + "", interest_comment_active, interest_comment_passive, interest_comment_content, interest_active_name, interest_passive_name);
                            }
                        }
                        if (start == 0) {
                            mySQLite.deleteInterestData();
                            mySQLite.deleteCommentData();
                            mySQLite.addInterestData(interestid, interestnumber, interestname, interestportrait, interestsex, interestcontent, interesttime, interestimage);
                        }
                        interest_data.setList(sublist);

                        list.add(interest_data);

                    }


                    if (type == 0) {
                        if (jsonArray.length() < 5) {
                            isAll = true;
                        }
                        handler.sendEmptyMessage(0);


                    } else if (type == 1) {
                        if (jsonArray.length() < 5) {
                            isAll = true;
                            handler.sendEmptyMessage(7);
                        } else {
                            isAll = false;
                            handler.sendEmptyMessage(1);
                        }

                    } else {
                        if (jsonArray.length() < 5) {
                            isAll = true;
                        }
                        handler.sendEmptyMessage(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(3);
                }

            }

            @Override
            public void onFailure(int s, Header[] headers, byte[] bytes, Throwable throwable) {
                handler.sendEmptyMessage(4);
            }
        });

    }


    private void update(String res) throws Exception {
        Interest_comment interest_comment = new Interest_comment();
        JSONObject js = new JSONObject(res);
        JSONObject it = js.getJSONObject("comment");
        int interest_commentid = it.getInt("interest_commentid");
        int interest_id = it.getInt("interestid");
        String interest_comment_active = it.getString("interest_comment_active");
        String interest_comment_content = it.getString("interest_comment_content");
        String interest_comment_passive = it.getString("interest_comment_passive");
        String interest_active_name = it.getString("interest_active_name");
        String interest_passive_name = it.getString("interest_passive_name");
        interest_comment.setInterest_commentid(interest_commentid);
        interest_comment.setInterestid(interest_id);
        interest_comment.setInterest_comment_active(interest_comment_active);
        interest_comment.setInterest_comment_passive(interest_comment_passive);
        interest_comment.setInterest_comment_content(interest_comment_content);
        interest_comment.setInterest_active_name(interest_active_name);
        interest_comment.setInterest_passive_name(interest_passive_name);
        Interest_data data = list.get(position);
        List<Interest_comment> lc = data.getList();
        lc.add(interest_comment);
        data.setList(lc);
        list.remove(position);
        list.add(position, data);
        mySQLite.addInterestComment(interest_commentid + "",
                interest_id + "", interest_comment_active, interest_comment_passive,
                interest_comment_content, interest_active_name, interest_passive_name);

    }
}
