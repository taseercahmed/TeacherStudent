package com.teacherstudent.Student;

public class StudentDiary_Setter {
    String SD_diaryDate,SD_diaryTime,SD_senderEmail,SD_diaryDetail;

    public StudentDiary_Setter(String SD_diaryDate, String SD_diaryTime, String SD_senderEmail, String SD_diaryDetail) {
        this.SD_diaryDate = SD_diaryDate;
        this.SD_diaryTime = SD_diaryTime;
        this.SD_senderEmail = SD_senderEmail;
        this.SD_diaryDetail = SD_diaryDetail;
    }

    public String getSD_diaryDate() {
        return SD_diaryDate;
    }

    public void setSD_diaryDate(String SD_diaryDate) {
        this.SD_diaryDate = SD_diaryDate;
    }

    public String getSD_diaryTime() {
        return SD_diaryTime;
    }

    public void setSD_diaryTime(String SD_diaryTime) {
        this.SD_diaryTime = SD_diaryTime;
    }

    public String getSD_senderEmail() {
        return SD_senderEmail;
    }

    public void setSD_senderEmail(String SD_senderEmail) {
        this.SD_senderEmail = SD_senderEmail;
    }

    public String getSD_diaryDetail() {
        return SD_diaryDetail;
    }

    public void setSD_diaryDetail(String SD_diaryDetail) {
        this.SD_diaryDetail = SD_diaryDetail;
    }
}
