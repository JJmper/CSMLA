package com.jlu.edu.mail.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengheming on 2016/4/11.
 */
public class Email implements Serializable{
    private static final long serialVersionUID = 1L;
    private String addressser;//发件人
    private String recepients;//收件人
    private String time;//时间
    private String subject;//主题
    private String content;//正文内容
    private List<String> attach;//附件
    private boolean isHtml;
    private String charset;
    public String getAddressser() {
        return addressser;
    }

    public void setAddressser(String addressser) {
        this.addressser = addressser;
    }

    public String getRecepients() {
        return recepients;
    }

    public void setRecepients(String recepients) {
        this.recepients = recepients;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAttach() {
        return attach;
    }

    public void setAttach(List<String> attach) {
        this.attach = attach;
    }



    public boolean isHtml() {
        return isHtml;
    }

    public void setIsHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
