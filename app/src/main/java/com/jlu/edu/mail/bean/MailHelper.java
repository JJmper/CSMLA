
package com.jlu.edu.mail.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import utils.MyApplication;


public class MailHelper {

    private static MailHelper instance;
    private List<MailReceiver> mailList;
    private HashMap<String, Integer> serviceHashMap;

    public static MailHelper getInstance() {
        if (instance == null) {
            instance = new MailHelper();
        }
        return instance;
    }

    /**
     *
     */
    private MailHelper() {

    }

    /**
     * 取得所有的邮件
     */
    public List<MailReceiver> getAllMail() throws MessagingException {
        List<MailReceiver> mailList = new ArrayList<>();

        // 连接服务器
        Store store = MyApplication.session.getStore(MyApplication.info.getType());
        store.connect(MyApplication.info.getMailServerHost(), MyApplication.info.getUserName(), MyApplication.info.getPassword());
        // 打开文件夹

        Folder folder;
        if ("pop3".equals(MyApplication.info.getType())) {
            folder = store.getFolder("INBOX");
        } else {
            folder =store.getFolder("INBOX");
        }
        folder.open(Folder.READ_ONLY);
        // 总的邮件数
        int mailCount = folder.getMessageCount();
        if (mailCount == 0) {
            folder.close(true);
            store.close();
            return null;
        } else {
            // 取得所有的邮件
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                MailReceiver reciveMail = new MailReceiver((MimeMessage) messages[i]);
                mailList.add(reciveMail);// 添加到邮件列表中
            }
            return mailList;
        }
    }
}
