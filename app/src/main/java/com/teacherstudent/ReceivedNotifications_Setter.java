package com.teacherstudent;

public class ReceivedNotifications_Setter {
    String notificationDate,notificationTime,notificationMsg,senderName,senderEmail,RN_notify,RN_key;

    public ReceivedNotifications_Setter(String notificationDate, String notificationTime, String notificationMsg, String senderName, String senderEmail, String RN_notify, String RN_key) {
        this.notificationDate = notificationDate;
        this.notificationTime = notificationTime;
        this.notificationMsg = notificationMsg;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.RN_notify = RN_notify;
        this.RN_key = RN_key;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationMsg() {
        return notificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        this.notificationMsg = notificationMsg;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRN_notify() {
        return RN_notify;
    }

    public void setRN_notify(String RN_notify) {
        this.RN_notify = RN_notify;
    }

    public String getRN_key() {
        return RN_key;
    }

    public void setRN_key(String RN_key) {
        this.RN_key = RN_key;
    }
}
