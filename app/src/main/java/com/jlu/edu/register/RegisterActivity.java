package com.jlu.edu.register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import HttpUtils.User_http;
import utils.CircularImage;
import utils.DesUtils;
import utils.Net;
import utils.PatternAuth;
import utils.SelectPicPopupWindow;
import utils.SpUtil;
import utils.UrlPath;

/**
 * Created by zhengheming on 2015/12/26.
 */
public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getName();
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private CircularImage portrait;
    private EditText number;
    private TextView text;
    private EditText name;
    private EditText password;
    private EditText email;
    private Button register;
    private boolean menu_display;
    private PopupWindow menuWindow;
    private Bitmap bitmap;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private InputStream Userportrait;
    private String Userclassify;
    private String Usernumber;
    private String Username;
    private String Userschool;
    private String Userpassword;
    private String Useremail;
    private String Usersex;
    private String Usertext = "学号";
    private DesUtils des = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        click();


    }

    private void init() {
        try {
            des = new DesUtils("csmla");//自定义密钥
        } catch (Exception e) {
            e.printStackTrace();
        }
        Userclassify = new SpUtil("User").send().getString("Userclassify", "student");
        Userschool = new SpUtil("User").send().getString("Userschool", "通信工程学院");
        Usersex = new SpUtil("User").send().getString("Usersex", "boy");
        portrait = (CircularImage) findViewById(R.id.register_portrait);
        number = (EditText) findViewById(R.id.register_number);
        text = (TextView) findViewById(R.id.register_text);
        if ("student".equals(Userclassify)) {
            Usertext = "学号";
        } else if ("teacher".equals(Userclassify)) {
            Usertext = "教学号";
        }
        text.setText(Usertext);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        register = (Button) findViewById(R.id.register_register);
    }

    private void click() {
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(RegisterActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(RegisterActivity.this.findViewById(R.id.register_), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!new Net().isNetworkAvailable(RegisterActivity.this)) {
                    Toast.makeText(RegisterActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                //将图片转成bitmap
                Bitmap f = ((BitmapDrawable) portrait.getDrawable()).getBitmap();
                if (f != null) {
                    getInputstream(f);
                } else {
                    Toast.makeText(RegisterActivity.this, "----", Toast.LENGTH_SHORT).show();
                }

                //头像获取并验证
                if (Userportrait != null) {

                    //身份获取并验证----默认

                    //学号/教工号获取并验证
                    Usernumber = number.getText().toString().trim();
                    if (PatternAuth.getAuth(Usernumber, "^[0-9]{8,15}$")) {

                        Username = name.getText().toString().trim();
                        //昵称获取并验证
                        if (PatternAuth.getAuth(Username, "[a-zA-Z0-9_]{3,10}")) {

                            //性别获取并验证----默认

                            //学院获取并验证

                            //密码获取并验证
                            Userpassword = password.getText().toString().trim();
                            if (PatternAuth.getAuth(Userpassword, "[a-zA-Z0-9]{8,14}")) {
                                //找回密码关键词获取并验证
                                Useremail = email.getText().toString().trim();
                                if (PatternAuth.email(Useremail)) {

                                    //申请注册

                                    register_post(Usernumber, Userportrait, Username, Userclassify, Usersex, Userpassword, Useremail, Userschool);

                                } else {
                                    Toast.makeText(RegisterActivity.this, "邮箱未设置或格式错误", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "密码未设置或格式错误", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "昵称未设置或格式错误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, Usertext + "未设置或格式错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "头像未设置", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void register_post(String usernumber, InputStream userportrait, String username, String userclassify, String usersex, String userpassword, String useremail, String userschool) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(), "注册成功",
                        Toast.LENGTH_LONG).show();
                new SpUtil("User").save().putString("Usernumber", Usernumber).putString("Userpassword", Userpassword).commit();
                new User_http(Usernumber).AsyncHttpService();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                RegisterActivity.this.finish();


            }
        };


        AsyncHttpClient client = new AsyncHttpClient();
        String Url = UrlPath.register;
        RequestParams params = new RequestParams();
        params.put("Usernumber", usernumber);
        params.put("Username", username);
        try {
            params.put("Userpassword", des.encrypt(userpassword));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("Usersex", usersex);
        params.put("Userclassify", userclassify);
        params.put("Userschool", userschool);
        params.put("Useremail", useremail);
        params.put("Userportrait", userportrait);

        client.post(Url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String res = new String(bytes);
                if ("1".equals(res)) {
                    handler.sendEmptyMessage(0);
                } else if ("-1".equals(res)) {
                    Toast.makeText(RegisterActivity.this, "该学号/教工号已被注册", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(RegisterActivity.this, i + "----", Toast.LENGTH_SHORT).show();
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
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(RegisterActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                this.portrait.setImageBitmap(bitmap);
                boolean delete = tempFile.delete();
                System.out.println("delete = " + delete);
            } catch (Exception e) {
                e.printStackTrace();
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
            if (photo != null && !photo.trim().equals("")) {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(photo.getBytes());
                Userportrait = tInputStringStream;

            }
            out.flush();
            out.close();
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    /**
     * 剪切图片
     *
     * @param uri
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}






