
package com.jlu.edu.mail.bean;

import android.util.Log;

import com.jlu.edu.mail.utils.TranCharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import Decoder.BASE64Decoder;


public class MailReceiver implements Serializable {
    private static final long serialVersionUID = 1L;
    private MimeMessage mimeMessage = null;
    private StringBuffer mailContent = new StringBuffer();// 邮件内容
    private String dataFormat = "yyyy-MM-dd HH:mm:ss";
    private String charset;
    private boolean html;
    private ArrayList<String> attachments = new ArrayList<>();
    private ArrayList<InputStream> attachmentsInputStreams = new ArrayList<>();

    public MailReceiver(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
        try {
            charset = parseCharset(mimeMessage.getContentType());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得送信人的姓名和邮件地址
     * }
     *
     * @throws Exception
     */
    public String getFrom() throws Exception {
        String nameAddr = "";
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String addr = address[0].getAddress();
        String name = address[0].getPersonal();
        if (addr == null) {
            addr = "";
        }
        if (name == null) {
            addr = subString(addr);
            nameAddr = addr;
        } else if (charset == null) {
            name = TranCharsetUtil.TranEncodeTOGB(name);
            nameAddr = name + "<" + addr + ">";
        }

        return nameAddr;
    }

    private String subString(String s) throws Exception {
        s = s.substring(0, s.indexOf("?="));
        s = s.substring(s.lastIndexOf('?') + 1, s.length());
        s = base64Decoder(s);

        return s;
    }

    // base64解码
    private String base64Decoder(String s) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);
        return (new String(b));
    }

    /**
     * 根据类型，获取邮件地址 "TO"--收件人地址 "CC"--抄送人地址 "BCC"--密送人地址
     *
     * @throws Exception
     */
    public String getMailAddress(String type) throws Exception {
        String mailAddr = "";
        String addType = type.toUpperCase(Locale.CHINA);
        InternetAddress[] address;
        if (addType.equals("TO")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
        } else if (addType.equals("CC")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
        } else if (addType.equals("BCC")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
        } else {
            System.out.println("error type!");
            throw new Exception("Error emailaddr type!");
        }
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                String mailaddress = address[i].getAddress();
                if (mailaddress != null) {
                    mailaddress = MimeUtility.decodeText(mailaddress);
                } else {
                    mailaddress = "";
                }
                String name = address[i].getPersonal();
                if (name != null) {
                    name = MimeUtility.decodeText(name);
                } else {
                    name = "";
                }
                mailAddr = name + "<" + mailaddress + ">";
            }
        }
        return mailAddr;
    }

    /**
     * 取得邮件标题
     *
     * @return String
     */
    public String getSubject() {
        String subject = "";
        try {
            subject = mimeMessage.getSubject();
            if (subject.indexOf("=?gb18030?") != -1) {
                subject = subject.replace("gb18030", "gb2312");
            }
            subject = MimeUtility.decodeText(subject);
            if (charset == null) {
                subject = TranCharsetUtil.TranEncodeTOGB(subject);
            }
        } catch (Exception e) {
        }
        Log.i("----------=========>>",subject);
        return subject;
    }

    /**
     * 取得邮件日期
     *
     * @throws MessagingException
     */
    public String getSentData() throws MessagingException {
        Date sentdata = mimeMessage.getSentDate();
        if (sentdata != null) {
            SimpleDateFormat format = new SimpleDateFormat(dataFormat, Locale.CHINA);
            return format.format(sentdata);
        } else {
            return "未知";
        }
    }

    /**
     * 取得邮件内容
     *
     * @throws Exception
     */
    public String getMailContent() throws Exception {
        compileMailContent(mimeMessage);

        String content = mailContent.toString();
        if (content.contains("<html>")) {
            html = true;
        }
        mailContent.setLength(0);
        content = MimeUtility.decodeText(content);
        return content;
    }


    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public boolean isHtml() {
        return html;
    }

    public ArrayList<InputStream> getAttachmentsInputStreams() {
        return attachmentsInputStreams;
    }

    /**
     * 解析邮件内容
     *
     * @param part
     * @throws Exception
     */
    private void compileMailContent(Part part) throws Exception {
        String contentType = part.getContentType();

        boolean connName = false;
        if (contentType.contains("name")) {
            connName = true;
        }
        if (part.isMimeType("text/plain") && !connName) {
            String type_text = part.getContentType();
            type_text=subType(type_text);
            String content = parseInputStream((InputStream) part.getContent(), type_text);
            mailContent.append(content);
        } else if (part.isMimeType("text/html") && !connName) {
            String type_html = part.getContentType();
            type_html=subType(type_html);
            html = true;
            String content = parseInputStream((InputStream) part.getContent(), type_html);
            mailContent.append(content);
        } else if (part.isMimeType("multipart/*") || part.isMimeType("message/rfc822")) {
            if (part.getContent() instanceof Multipart) {
                Multipart multipart = (Multipart) part.getContent();
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    compileMailContent(multipart.getBodyPart(i));
                }
            } else {
                Multipart multipart = new MimeMultipart(new ByteArrayDataSource(part.getInputStream(), "multipart/*"));
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    compileMailContent(multipart.getBodyPart(i));
                }
            }
        } else if (part.getDisposition() != null && part.getDisposition().equals(Part.ATTACHMENT)) {
            // 获取附件

            String filename = part.getFileName();
            if (filename != null) {
                if (filename.contains("=?gb18030?")) {
                    filename = filename.replace("gb18030", "gb2312");
                }
                filename = MimeUtility.decodeText(filename);
                attachments.add(filename);
                attachmentsInputStreams.add(part.getInputStream());
            }

        }
    }

    /**
     * 解析字符集编码
     */
    private String parseCharset(String contentType) {
        if (!contentType.contains("charset")) {
            return null;
        }
        if (contentType.contains("gbk")) {
            return "GBK";
        } else if (contentType.contains("GB2312") || contentType.contains("gb18030")) {
            return "gb2312";
        } else {
            String sub = contentType.substring(contentType.indexOf("charset") + 8).replace("\"", "");
            if (sub.contains(";")) {
                return sub.substring(0, sub.indexOf(";"));
            } else {
                return sub;
            }
        }
    }

    /**
     * 解析流格式
     */
    private String parseInputStream(InputStream in, String type) throws IOException, MessagingException {
        StringBuilder str = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(in, type);
        BufferedReader br = new BufferedReader(isr);
        String strLine;
        try {
            while ((strLine = br.readLine()) != null) {

                if (charset == null) {
                    String s = new String(strLine.getBytes(type), type);
                    str.append(s);
                } else {
                    str.append(new String(strLine.getBytes(), 0, strLine.length(), charset));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public String getCharset() {
        return charset;
    }

    private String subType(String type) {
        Log.i("----------=========>>",type);
        String res;
        if (type.contains("\"")) {
            res = type.substring(type.indexOf("=") + 2, type.length() - 1);
        }else{
            res = type.substring(type.indexOf("=") + 1, type.length());
        }
        Log.i("----------=========>>",res);
        return res;
    }


}
