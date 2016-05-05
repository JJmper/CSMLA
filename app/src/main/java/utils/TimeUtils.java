package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhengheming on 2016/2/22.
 */
public class TimeUtils {
    public static String formatDate(String str) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf1.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdf2.format(d);
    }

    public static long formatSecond(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = sdf.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
    }

    public static String formatSecond(long str) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(str));


    }
}
