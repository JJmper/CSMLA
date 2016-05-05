package utils;

/**
 * 类型转换工具
 * Created by zhengheming on 2016/2/20.
 */
public class ChangeType {
    /**
     * 字符串类型转成整型类型
     *
     * @param s
     * @return
     */
    public static Integer change_S_I(String s) {
        return Integer.valueOf(s);
    }

    /**
     * 整型类型转成字符串类型
     *
     * @param s
     * @return
     */
    public static String change_I_S(int i) {
        return String.valueOf(i);
    }


}
