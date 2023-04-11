package com.bib.sendmail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class EmailGUI extends JFrame {
    private JTextField txtTo;
    private JTextField txtCc;
    private JTextField txtBcc;
    private JTextField txtSubject;
    private JTextArea txtMessage;
    private JButton btnSend;
    private JTextField txtDirectory;
    private JButton btnAttach;
    private JPanel panelMain;
    public String from = "jeymarjordan@outlook.com", pw = "09613851225Jmar";

    public EmailGUI() {
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        btnAttach.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
    }

    public static void main(String[] args) {
        EmailGUI sender = new EmailGUI();
        sender.setContentPane(sender.panelMain);
        sender.setTitle("Email Sender V1");
        sender.setSize(900, 600);
        sender.setVisible(true);
        sender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void sendMessage() {
        try {
            DataChecker checker = new DataChecker();
            MimeMessage message = new MimeMessage(Server.createSession(from,pw));
            message.setFrom(new InternetAddress(from));

            boolean toEmpty = checker.validateField(txtTo);
            boolean ccEmpty = checker.validateField(txtCc);
            boolean bccEmpty = checker.validateField(txtBcc);
            boolean subjectEmpty = checker.validateField(txtSubject);
            boolean messageEmpty = checker.validateMessage(txtMessage);

            if(subjectEmpty || messageEmpty){
                JOptionPane.showMessageDialog(null, ErrorMessage.showErrorForEmptyMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(toEmpty && ccEmpty && bccEmpty){
                JOptionPane.showMessageDialog(null, ErrorMessage.showErrorForEmptyRecipient(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] toAccount = formatRecipients(txtTo.getText());
            String[] ccAccount = formatRecipients(txtCc.getText());
            String[] bccAccount = formatRecipients(txtBcc.getText());

            boolean validTo = checker.validateRecipients(toEmpty, toAccount);
            boolean validCc = checker.validateRecipients(ccEmpty, ccAccount);
            boolean validBcc = checker.validateRecipients(bccEmpty, bccAccount);

            message.setSubject(txtSubject.getText());
            message.setText(txtMessage.getText());


            if(!validTo || !validCc || !validBcc){
                JOptionPane.showMessageDialog(null, ErrorMessage.showErrorForInvalidEmail(validTo, validCc, validBcc), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            addRecipients(Message.RecipientType.TO, message, toAccount, toEmpty);
            addRecipients(Message.RecipientType.CC, message, ccAccount, ccEmpty);
            addRecipients(Message.RecipientType.BCC, message, bccAccount, bccEmpty);


            if (!(txtDirectory.getText().equals(""))) {

                Multipart multipart = new MimeMultipart();
                MimeBodyPart attachmentPart = new MimeBodyPart();
                MimeBodyPart textPart = new MimeBodyPart();
                try {
                    File f = new File(txtDirectory.getText());
                    attachmentPart.attachFile(f);
                    textPart.setText(txtMessage.getText());
                    multipart.addBodyPart(textPart);
                    multipart.addBodyPart(attachmentPart);

                } catch (IOException e) {

                    e.printStackTrace();
                }
                message.setContent(multipart);
            }


            try {
                System.out.println("sending...");
//                 Send message
                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (Exception e) {
                e.printStackTrace();
            }
            clearFields();

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(panelMain);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            txtDirectory.setText(f.getAbsolutePath());
        }
    }

    public void clearFields() {
        txtTo.setText(null);
        txtCc.setText(null);
        txtBcc.setText(null);
        txtSubject.setText(null);
        txtMessage.setText(null);
        txtDirectory.setText(null);
    }

    public String[] formatRecipients(String accounts){
        accounts = accounts.replaceAll(" ", "");
        String [] rcp = accounts.split(",");
        return rcp;
    }

    public void addRecipients(Message.RecipientType type, MimeMessage message, String[] accounts, boolean empty) throws MessagingException {
        if(empty)
            return;
        else {
            for (int i = 0; i < accounts.length; i++) {
                message.addRecipient(type, new InternetAddress(accounts[i]));
            }
        }
    }
}
