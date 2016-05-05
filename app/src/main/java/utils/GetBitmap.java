package utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengheming on 2016/5/5.
 */
public class GetBitmap {


        /**
         * 文件转为bitmap
         */
        public Bitmap returnBitmap(Context context, Uri uri) {

            // 1.加载位图
            InputStream stream = null;
            Bitmap btp = null;
            try {
                stream = new FileInputStream(getRealFilePath(context,uri));
                // 2.为位图设置100K的缓存
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inTempStorage = new byte[100 * 1024];
                // 3.设置位图颜色显示优化方式
                // ALPHA_8：每个像素占用1byte内存（8位）
                // ARGB_4444:每个像素占用2byte内存（16位）
                // ARGB_8888:每个像素占用4byte内存（32位）
                // RGB_565:每个像素占用2byte内存（16位）
                // Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4
                // bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                // 4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
                opts.inPurgeable = true;
                // 5.设置位图缩放比例
                // width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
                opts.inSampleSize = 4;
                // 6.设置解码位图的尺寸信息
                opts.inInputShareable = true;
                // 7.解码位图
                btp = BitmapFactory.decodeStream(stream, null, opts);
                stream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 8.显示位图
            return btp;
        }

        /**
         * 根据Uri获取文件的路径
         *
         * @param context
         * @param uri
         * @return
         */
        private  String getRealFilePath(Context context, Uri uri) {
            if (null == uri)
                return null;

            final String scheme = uri.getScheme();
            String path = null;
            if (scheme == null)
                path = uri.getPath();
            else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                path = uri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null,
                        null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index > -1) {
                            path = cursor.getString(index);
                        }
                    }
                    cursor.close();
                }
            }
            return path;
        }
}
