package com.jlu.edu.mail.bean;

import com.jlu.edu.mail.utils.ProperticsUtils;

import java.io.Serializable;
import java.util.Properties;

/**
 * 邮件的基本信息
 * @author Administrator
 *
 */
public class MailInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
    private String type;

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		if("pop3".equals(type)){
			p.put("mail.pop3.host", this.mailServerHost);
			p.put("mail.pop3.port", this.mailServerPort);
			p.put("mail.pop3.auth",  "true");
		}else{
          p= ProperticsUtils.getEncryptPropertics(mailServerHost);
		}

		return p;
	}


	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
