package com.teacherstudent.Student;

public class S_Notes_Setter {
    String S_Notes_date,S_Notes_messageDetail,S_Notes_senderEmail,S_Notes_time,S_Notes_msgType;

    public S_Notes_Setter(String s_Notes_date, String s_Notes_messageDetail, String s_Notes_senderEmail, String s_Notes_time, String s_Notes_msgType) {
        S_Notes_date = s_Notes_date;
        S_Notes_messageDetail = s_Notes_messageDetail;
        S_Notes_senderEmail = s_Notes_senderEmail;
        S_Notes_time = s_Notes_time;
        S_Notes_msgType = s_Notes_msgType;
    }

    public String getS_Notes_date() {
        return S_Notes_date;
    }

    public void setS_Notes_date(String s_Notes_date) {
        S_Notes_date = s_Notes_date;
    }

    public String getS_Notes_messageDetail() {
        return S_Notes_messageDetail;
    }

    public void setS_Notes_messageDetail(String s_Notes_messageDetail) {
        S_Notes_messageDetail = s_Notes_messageDetail;
    }

    public String getS_Notes_senderEmail() {
        return S_Notes_senderEmail;
    }

    public void setS_Notes_senderEmail(String s_Notes_senderEmail) {
        S_Notes_senderEmail = s_Notes_senderEmail;
    }

    public String getS_Notes_time() {
        return S_Notes_time;
    }

    public void setS_Notes_time(String s_Notes_time) {
        S_Notes_time = s_Notes_time;
    }

    public String getS_Notes_msgType() {
        return S_Notes_msgType;
    }

    public void setS_Notes_msgType(String s_Notes_msgType) {
        S_Notes_msgType = s_Notes_msgType;
    }
}
