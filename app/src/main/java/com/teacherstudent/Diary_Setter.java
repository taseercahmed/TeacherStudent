package com.teacherstudent;

public class Diary_Setter {
    String diaryDate,diaryTime,diaryDetail;

    public Diary_Setter(String diaryDate, String diaryTime, String diaryDetail) {
        this.diaryDate = diaryDate;
        this.diaryTime = diaryTime;
        this.diaryDetail = diaryDetail;
    }

    public String getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(String diaryDate) {
        this.diaryDate = diaryDate;
    }

    public String getDiaryTime() {
        return diaryTime;
    }

    public void setDiaryTime(String diaryTime) {
        this.diaryTime = diaryTime;
    }

    public String getDiaryDetail() {
        return diaryDetail;
    }

    public void setDiaryDetail(String diaryDetail) {
        this.diaryDetail = diaryDetail;
    }
}
