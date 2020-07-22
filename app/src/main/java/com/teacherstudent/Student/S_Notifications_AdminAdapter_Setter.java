package com.teacherstudent.Student;

public class S_Notifications_AdminAdapter_Setter {
    String SN_date,SN_message,SN_senderEmail, SN_senderName,SN_time,SN_notify,SN_key;

    public S_Notifications_AdminAdapter_Setter(String SN_date, String SN_message, String SN_senderEmail, String SN_senderName, String SN_time, String SN_notify, String SN_key) {
        this.SN_date = SN_date;
        this.SN_message = SN_message;
        this.SN_senderEmail = SN_senderEmail;
        this.SN_senderName = SN_senderName;
        this.SN_time = SN_time;
        this.SN_notify = SN_notify;
        this.SN_key = SN_key;
    }

    public String getSN_date() {
        return SN_date;
    }

    public void setSN_date(String SN_date) {
        this.SN_date = SN_date;
    }

    public String getSN_message() {
        return SN_message;
    }

    public void setSN_message(String SN_message) {
        this.SN_message = SN_message;
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

    public String getSN_time() {
        return SN_time;
    }

    public void setSN_time(String SN_time) {
        this.SN_time = SN_time;
    }

    public String getSN_notify() {
        return SN_notify;
    }

    public void setSN_notify(String SN_notify) {
        this.SN_notify = SN_notify;
    }

    public String getSN_key() {
        return SN_key;
    }

    public void setSN_key(String SN_key) {
        this.SN_key = SN_key;
    }
}
