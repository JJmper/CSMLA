package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则
 * Created by 郑和明 on 2015/11/18.
 */
public class PatternAuth {
    //正则表达式
    public static boolean getAuth(String s1, String s2) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(s2);
        Matcher matcher = pattern.matcher(s1);
        flag = matcher.matches();
        return flag;

    }

    //检查是否为邮箱
    public static boolean email(String s1) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$");
        Matcher matcher = pattern.matcher(s1);
        flag = matcher.matches();
        return flag;
    }
    public static boolean getemail(String s1, String s2) {
        Pattern pattern = Pattern.compile(s2);
        Matcher matcher = pattern.matcher(s1);
        while(matcher.find()){
            return true;
        }
        return false;

    }
}
