package com.teacherstudent;

public class SentNotifications_Setter {
    String SN_date,SN_messae,SN_receiverEmails,SN_sendedTo,SN_senderEmail,SN_senderName,SN_subject,SN_time;

    public SentNotifications_Setter(String SN_date, String SN_messae, String SN_receiverEmails, String SN_sendedTo, String SN_senderEmail, String SN_senderName, String SN_subject, String SN_time) {
        this.SN_date = SN_date;
        this.SN_messae = SN_messae;
        this.SN_receiverEmails = SN_receiverEmails;
        this.SN_sendedTo = SN_sendedTo;
        this.SN_senderEmail = SN_senderEmail;
        this.SN_senderName = SN_senderName;
        this.SN_subject = SN_subject;
        this.SN_time = SN_time;
    }

    public String getSN_date() {
        return SN_date;
    }

    public void setSN_date(String SN_date) {
        this.SN_date = SN_date;
    }

    public String getSN_messae() {
        return SN_messae;
    }

    public void setSN_messae(String SN_messae) {
        this.SN_messae = SN_messae;
    }

    public String getSN_receiverEmails() {
        return SN_receiverEmails;
    }

    public void setSN_receiverEmails(String SN_receiverEmails) {
        this.SN_receiverEmails = SN_receiverEmails;
    }

    public String getSN_sendedTo() {
        return SN_sendedTo;
    }

    public void setSN_sendedTo(String SN_sendedTo) {
        this.SN_sendedTo = SN_sendedTo;
    }

    public String getSN_senderEmail() {
        return SN_senderEmail;
    }

    public void setSN_senderEmail(String SN_senderEmail) {
        this.SN_senderEmail = SN_senderEmail;
    }

    public String getSN_senderName() {
        return SN_senderName;
    }

    public void setSN_senderName(String SN_senderName) {
        this.SN_senderName = SN_senderName;
    }

    public String getSN_subject() {
        return SN_subject;
    }

    public void setSN_subject(String SN_subject) {
        this.SN_subject = SN_subject;
    }

    public String getSN_time() {
        return SN_time;
    }

    public void setSN_time(String SN_time) {
        this.SN_time = SN_time;
    }
}
