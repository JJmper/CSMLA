package com.jlu.edu.syllabus.domain;

import utils.SpUtil;

/**
 * Created by zhengheming on 2016/2/29.
 */
public class Course {
    private String usernumber;
    private String name;//课堂名称
    private String place;//地点
    private String teacher;//教师名称
    private int step;       //课程节数
    private int start;       //课程开始时节次
    private int week;
    public Course() {
        usernumber=new SpUtil("User").send().getString("Usernumber","");
    }
    public Course(String name,String teacher,String place,int week,int start,int step){
        usernumber=new SpUtil("User").send().getString("Usernumber","");
        this.name = name;
        this.place = place;
        this.start = start;
        this.step = step;
        this.teacher = teacher;
        this.week=week;
    }

    public String getUsernumber() {
        return usernumber;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
