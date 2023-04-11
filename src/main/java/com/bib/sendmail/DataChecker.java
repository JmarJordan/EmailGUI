package com.bib.sendmail;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataChecker {

    public boolean validateField(JTextField tf){

        if(tf.getText().equals(""))
            return true;
        else
            return false;
    }
    public boolean validateMessage(JTextArea ta){

        if(ta.getText().equals(""))
            return true;
        else
            return false;
    }
    public boolean validateRecipients(boolean isEmpty, String [] accounts){
        boolean valid = true;
        if(!isEmpty){
            String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(regexPattern);
            for (String account : accounts){
                Matcher matcher = pattern.matcher(account);
                if (matcher.matches())
                    continue;
                else {
                    valid = false;
                }
            }
        }
        return valid;
    }
}
