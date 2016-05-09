package com.jlu.edu.interest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jlu.edu.csmla.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import utils.GetBitmap;
import utils.Net;
import utils.SelectPicPopupWindow;
import utils.SpUtil;
import utils.SysActivity;
import utils.UrlPath;


/**
 * Created by zhengheming on 2016/1/20.
 */
public class SendMessageActivity extends Activity {
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private EditText content;
    private ImageView picture;
    private TextView send;
    private TextView back;
    private PopupWindow menuWindow;
    private Bitmap bitmap;
    private InputStream image = null;
    private Drawable drawable;
    private boolean flag = false;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_send);
        init();
        Click();
    }

    private void init() {
        content = (EditText) findViewById(R.id.interest_send_edit);
        picture = (ImageView) findViewById(R.id.interest_send_picture);
        drawable = picture.getDrawable();
        send = (TextView) findViewById(R.id.interest_send_send);
        back = (TextView) findViewById(R.id.interest_send_back);
    }

    private void Click() {

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(SendMessageActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(SendMessageActivity.this.findViewById(R.id.interest_send_), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Net.isNetworkAvailable(SendMessageActivity.this)) {
                    Toast.makeText(SendMessageActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                //将图片转成bitmap
                if (picture.getDrawable() != drawable) {
                    Bitmap f = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                    getInputstream(f);
                } else {
                    flag = true;

                }
                String text = content.getText().toString().trim();
                if (!"".equals(text)) {
                    String usernumber = new SpUtil("User").send().getString("Usernumber", "").trim();
                    interest_send(usernumber, new Date().getTime(), text);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void interest_send(String name, long time, String content) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(), "发表成功",
                        Toast.LENGTH_LONG).show();
                SysActivity.getInstance().exit("InterestActivity");
                Intent intent = new Intent(SendMessageActivity.this, InterestActivity.class);
                SendMessageActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                finish();


            }
        };


        AsyncHttpClient client = new AsyncHttpClient();
        String Url;
        RequestParams params = new RequestParams();
        if (flag) {
            Url = UrlPath.AddInterestNO;
            params.put("Interestnumber", name);
            params.put("Interesttime", String.valueOf(time));
            params.put("Interestcontent", content);
        } else {
            Url = UrlPath.AddInterestData;
            params.put("Interestnumber", name);
            params.put("Interesttime", String.valueOf(time));
            params.put("Interestcontent", content);
            params.put("Interestimage", image);
        }

        client.post(Url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);

                if ("1".equals(res)) {
                    handler.sendEmptyMessage(0);
                } else {
                    Toast.makeText(SendMessageActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(SendMessageActivity.this, i + "----", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Intent intentp = new Intent("android.media.action.IMAGE_CAPTURE");
                    // 判断存储卡是否可以用，可用进行存储
                    if (hasSdcard()) {
                        intentp.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                    }
                    startActivityForResult(intentp, PHOTO_REQUEST_CAMERA);
                    break;
                case R.id.btn_pick_photo:
                    // 激活系统图库，选择一张图片
                    Intent intentt = new Intent(Intent.ACTION_PICK);
                    intentt.setType("image/*");
                    startActivityForResult(intentt, PHOTO_REQUEST_GALLERY);
                    break;
                default:
                    break;
            }


        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                bitmap = new GetBitmap().returnBitmap(SendMessageActivity.this, uri);
                this.picture.setImageBitmap(bitmap);

            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                bitmap = new GetBitmap().returnBitmap(SendMessageActivity.this, Uri.fromFile(tempFile));
                this.picture.setImageBitmap(bitmap);
                tempFile.delete();

                // crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(SendMessageActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //转成图片流
    private void getInputstream(Bitmap bp) {


        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, out);

            byte[] buffer = out.toByteArray();
            byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
            String photo = new String(encode);
            if (!photo.trim().equals("")) {
                image = new ByteArrayInputStream(photo.getBytes());
            }
            out.flush();
            out.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }


    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }
}
