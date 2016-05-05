package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 刷新头时间
 * Created by zhengheming on 2016/1/12.
 */
public class CurrentTime_Utils {
    public static String GetCurrentTime() {
        long time=new SpUtil("time").send().getLong("time",0);
        long lasttime = time;
        Date current = new Date();
        time = current.getTime();
        new SpUtil("time").save().putLong("time",time).commit();
        return Time(time, lasttime);
    }

    private static String Time(long currenttime, long lasttime) {
        String res=null;
        long puls=currenttime-lasttime;
        if(puls<15){
            res="刚刚刷新";
        }else if(puls>=15&&puls<60){
            res="一分钟内";
        }else if(puls>=60&&puls<120){
            res="二分钟内";
        }else if(puls>=120&&puls<300){
            res="五分钟内";
        }else{
            res=transferLongToDate(lasttime);
        }
        return res;
    }
    public static  String transferLongToDate(Long millSec){

        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

        Date date= new Date(millSec);

        return sdf.format(date);

    }
}
