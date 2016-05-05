package com.jlu.edu.mail.utils;

import java.security.Security;
import java.util.Properties;

public class ProperticsUtils {

    public static Properties getEncryptPropertics(String host) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.imap.socketFactory.fallback ", "false");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.socketFactory.port", "993");
        props.setProperty("mail.imap.ssl.enble", "true");
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.auth.plain.disable", "true");
        props.setProperty("mail.imap.host", host);
        return props;
    }
}
