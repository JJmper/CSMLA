package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *检查第一个是否是字母还是汉字
 * Created by zhengheming on 2016/1/31.
 */
public class checkType {
    public static boolean checkType_E(String content) {
        String first = content.charAt(0) + "";
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(first);
        if (m.matches()) {

            return true;
        }
        return false;

    }

    public static boolean checkType_Z(String content) {
        String first = content.charAt(0) + "";
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(first);
        if (m.matches()) {
            return true;
        }
        return false;
    }
}
