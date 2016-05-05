package com.jlu.edu.mail.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenricator extends Authenticator {  
    private String user=null;  
    private String pass=null;  
    public MyAuthenricator(String user, String pass){
        this.user=user;  
        this.pass=pass;  
    }  
    @Override  
    protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication(user,pass);  
    }  
      
}
