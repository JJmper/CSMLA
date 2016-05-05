package com.jlu.edu.interest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.util.LruCache;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.jlu.edu.csmla.R;
import com.jlu.edu.domain.Interest_comment;
import com.jlu.edu.domain.Interest_data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import utils.UrlPath;

/**
 * 如何摆脱item下的评论的混叠问题，(ko)
 * 滑动时数据不匹配问题(ko)
 * 评论后位置不动，跟踪列表位置(ko)
 * 服务器与客户端汉字编码问题（真机模拟）(ko)
 * <p/>
 * <p/>
 * Created by zhengheming on 2016/1/12.
 */
public class Interest_Adapter extends BaseAdapter {
    private Context context;
    private List<Interest_data> data;
    private RequestQueue queue;
    private ImageLoader mImageLoader;
    private SpannableString msp = null;
    private Comment comment;

    public Interest_Adapter(Context context, List<Interest_data> data) {
        this.context = context;
        this.data = data;
        queue = Volley.newRequestQueue(context);
        final LruCache<String, Bitmap> mImageCache = new LruCache<>(
                20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                mImageCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return mImageCache.get(key);
            }
        };
        mImageLoader = new ImageLoader(queue, imageCache);
    }


    public void onDateChange(List<Interest_data> data) {
        this.data = data;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.interest_item, null);
            viewHolder.item_interest_divider = (LinearLayout) convertView.findViewById(R.id.interest_divider);
            viewHolder.item_portrait = (NetworkImageView) convertView.findViewById(R.id.interest_item_head);
            viewHolder.item_name = (TextView) convertView.findViewById(R.id.interest_item_name);
            viewHolder.item_sex = (ImageView) convertView.findViewById(R.id.interest_item_sex);
            viewHolder.item_time = (TextView) convertView.findViewById(R.id.interest_item_time);
            viewHolder.item_text = (TextView) convertView.findViewById(R.id.interest_item_text);
            viewHolder.item_image = (NetworkImageView) convertView.findViewById(R.id.interest_item_image);
            viewHolder.item_share = (ImageView) convertView.findViewById(R.id.interest_item_share);
            viewHolder.item_comment = (ImageView) convertView.findViewById(R.id.interest_item_comment);
            viewHolder.item_comment_data = (LinearLayout) convertView.findViewById(R.id.interest_item_comment_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Interest_data id = data.get(position);
        final String number = id.getInterestnumber();
        String image = id.getInterestimage();
        String name = id.getInterestname();
        String text = id.getInterestcontent();
        String time = new SimpleDateFormat("yy-MM-dd hh:mm:ss").format(new Date(id.getInteresttime()));
        final String interestid = id.getInterestid() + "";
        final String sex = id.getInterestsex();
        List<Interest_comment> list = id.getList();
        int length = list.size();
        viewHolder.item_name.setText(name);
        //异步缓存网络图片
        //头像
        if (position == 0) {
            viewHolder.item_interest_divider.setVisibility(View.GONE);
        }
        viewHolder.item_portrait.setImageUrl(UrlPath.picture + id.getInterestportrait(), mImageLoader);
        viewHolder.item_portrait.setDefaultImageResId(R.mipmap.icon_stub);
        if ("boy".equals(sex)) {
            viewHolder.item_sex.setImageResource(R.mipmap.male_icon);
        } else {
            viewHolder.item_sex.setImageResource(R.mipmap.female_icon);
        }
        viewHolder.item_time.setText(time);
        viewHolder.item_text.setText(text);
        //图片
        if (image.equals("NO")) {
            viewHolder.item_image.setVisibility(View.GONE);
        } else {
            viewHolder.item_image.setImageUrl(UrlPath.picture + image, mImageLoader);
            viewHolder.item_image.setDefaultImageResId(R.mipmap.icon_empty);
        }
        if (viewHolder.item_comment_data.getChildCount() != length) {
            viewHolder.item_comment_data.removeAllViews();
            for (int i = 0; i < length; i++) {
                final Interest_comment cd = list.get(i);
                TextView textView = new TextView(context);
                textView.setBackgroundResource(R.drawable.comment_bg);
                final String active =cd.getInterest_active_name() ;
                String passive =cd.getInterest_passive_name();
                String content = cd.getInterest_comment_content();
                int act = active.length();
                int pass = passive.length();
                if (passive != null) {
                    msp = new SpannableString(active + "回复" + passive + ":" + content);
                    msp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, act, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
                    msp.setSpan(new ForegroundColorSpan(Color.BLUE), act + 2, act + pass + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
                } else {
                    msp = new SpannableString(active + ":" + content);
                    msp.setSpan(new ForegroundColorSpan(Color.BLUE), 0, act, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色
                }
                textView.setText(msp);
                viewHolder.item_comment_data.addView(textView);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onComment(interestid, cd.getInterest_comment_active(), true,position);

                    }
                });
            }

        }
        viewHolder.item_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComment(interestid, number, false,position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        NetworkImageView item_portrait;
        TextView item_name;
        ImageView item_sex;
        TextView item_time;
        TextView item_text;
        NetworkImageView item_image;
        ImageView item_share;
        ImageView item_comment;
        LinearLayout item_comment_data;
        LinearLayout item_interest_divider;
    }

    //点击item无反应
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;

    }

    public interface Comment {
        public void onComment(String interestid, String activenumber, boolean flag,int position);
    }

    public void SetOnCommentListener(Comment comment) {
        this.comment = comment;
    }

    public void onComment(String interestid_, String activenumber, boolean flag,int position) {
        if (comment != null) {
            comment.onComment(interestid_, activenumber, flag,position);
        }
    }
}