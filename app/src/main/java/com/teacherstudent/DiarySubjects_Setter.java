package com.teacherstudent;

public class DiarySubjects_Setter {
    String DiaryClassName,DiarySectionName,DiaryStudyGroup,DiarySubjectName;

    public DiarySubjects_Setter(String diaryClassName, String diarySectionName, String diaryStudyGroup, String diarySubjectName) {
        DiaryClassName = diaryClassName;
        DiarySectionName = diarySectionName;
        DiaryStudyGroup = diaryStudyGroup;
        DiarySubjectName = diarySubjectName;
    }

    public String getDiaryClassName() {
        return DiaryClassName;
    }

    public void setDiaryClassName(String diaryClassName) {
        DiaryClassName = diaryClassName;
    }

    public String getDiarySectionName() {
        return DiarySectionName;
    }

    public void setDiarySectionName(String diarySectionName) {
        DiarySectionName = diarySectionName;
    }

    public String getDiaryStudyGroup() {
        return DiaryStudyGroup;
    }

    public void setDiaryStudyGroup(String diaryStudyGroup) {
        DiaryStudyGroup = diaryStudyGroup;
    }

    public String getDiarySubjectName() {
        return DiarySubjectName;
    }

    public void setDiarySubjectName(String diarySubjectName) {
        DiarySubjectName = diarySubjectName;
    }
}
