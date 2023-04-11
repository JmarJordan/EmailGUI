package com.bib.sendmail;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class Server {

    public static Session createSession(String username,String password){
        //Adding Properties
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(username, password);

            }

        });
        // Used to debug SMTP issues
        session.setDebug(true);

        return session;
    }
}
