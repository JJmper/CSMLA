package utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具
 * Created by zhengheming on 2015/12/31.
 */
public class SpUtil {
    private String name;
   public SpUtil(String name){
        this.name=name;
    }
   public  SharedPreferences.Editor save(){
       SharedPreferences sp = MyApplication.getAppContext().getSharedPreferences(name,
               Activity.MODE_PRIVATE);
       SharedPreferences.Editor editor = sp.edit();
       return editor;
   }
    public  SharedPreferences send(){
        SharedPreferences sp = MyApplication.getAppContext().getSharedPreferences(name,
                Activity.MODE_PRIVATE);
        return sp;
    }
}
