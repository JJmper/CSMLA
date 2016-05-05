package com.jlu.edu.syllabus.utils;

import java.util.Calendar;

import utils.TimeUtils;

/**
 * Created by zhengheming on 2016/3/9.
 */
public class SyllabusTimeChange {
    //数值转化成开始时间
    public static String Change(int start) {
        String res = "";
        switch (start) {
            case 1:
                res = "08:00";
                break;
            case 2:
                res = "08:55";
                break;
            case 3:
                res = "10:00";
                break;
            case 5:
                res = "13:30";
                break;
            case 6:
                res = "14:25";
                break;
            case 7:
                res = "15:30";
                break;
            case 9:
                res = "18:00";
                break;
        }
        return res;
    }

    //将数值转化成结束时间
    public static String Change(int start, int end) {

        long temp = TimeUtils.formatSecond(Change(start));
        long middle = 0;
        if (end == 2) {
            middle = 100 * 60000;
        } else if (end == 3) {
            middle = 165 * 60000;
        } else if (end == 4) {
            middle = 220 * 60000;
        }

        return TimeUtils.formatSecond(middle + temp);
    }


    //开始时间转化成数值
    public static int Change(String temp) {
        int res = 0;
        switch (temp) {
            case "08:00":
                res = 1;
                break;
            case "10:00":
                res = 3;
                break;
            case "13:30":
                res = 5;
                break;
            case "15:30":
                res = 7;
                break;
            case "18:00":
                res = 9;
                break;
        }
        return res;
    }

    //将时间转化成课程节次
    public static int Change(String start, String end) {

        int res = 0;
        long sec = TimeUtils.formatSecond(end) - TimeUtils.formatSecond(start);
        int min = (int) (sec / 60000);
        switch (min) {
            case 100:
                res = 2;
                break;
            case 165:
                res = 3;
                break;
            case 220:
                res = 4;
                break;

        }
        return res;
    }

    public static int getWeek() {
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK);
        if (week == 1) {
            week = 7;
        } else if (week == 2) {
            week = 1;
        } else if (week == 3) {
            week = 2;
        } else if (week == 4) {
            week = 3;
        } else if (week == 5) {
            week = 4;
        } else if (week == 6) {
            week = 5;
        } else if (week == 7) {
            week = 6;
        }
        return week;
    }
}
