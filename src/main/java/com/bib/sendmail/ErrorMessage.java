package com.bib.sendmail;

public class ErrorMessage {

    public static String showErrorForEmptyMessage(){

        return "Please complete the fields.";
    }
    public static String showErrorForEmptyRecipient(){

        return "No recipient found.";
    }
    public static String showErrorForInvalidEmail(boolean to, boolean cc, boolean bcc){
        String err = "Invalid email entered for ";
        if(!to)
            err += "'TO' ";
        if(!cc)
            err += "'CC' ";
        if(!bcc)
            err += "'BCC' ";
        return err;
    }

}
