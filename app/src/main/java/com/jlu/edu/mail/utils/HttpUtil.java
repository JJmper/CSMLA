package com.jlu.edu.mail.utils;

import com.jlu.edu.mail.bean.MailInfo;
import com.jlu.edu.mail.bean.MyAuthenricator;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import utils.MyApplication;

/**
 * 为扩展邮箱的支持量，降低代码的重复修改率，封装。。。
 * Created by zhengheming on 2016/4/17.
 */
public class HttpUtil {

    public Session login() {
        Session session = isLoginRight(MyApplication.info);
        return session;
    }

    private Session isLoginRight(MailInfo info) {
        //判断是否要登入验证
        MyAuthenricator authenticator = new MyAuthenricator(info.getUserName(), info.getPassword());
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getDefaultInstance(info.getProperties(), authenticator);
        try {
            Store store;
            store = session.getStore(MyApplication.info.getType());
            store.connect(MyApplication.info.getMailServerHost(), MyApplication.info.getUserName(), MyApplication.info.getPassword());
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
        return session;
    }

}
