package com.teacherstudent;

public class StudentResultclass {
    String subjectname,marks,grade,totalmarks,teachername;

    public StudentResultclass() {
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTotalmarks() {
        return totalmarks;
    }

    public void setTotalmarks(String totalmarks) {
        this.totalmarks = totalmarks;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public StudentResultclass(String subjectname, String marks, String grade, String totalmarks, String teachername) {
        this.subjectname = subjectname;
        this.marks = marks;
        this.grade = grade;
        this.totalmarks = totalmarks;
        this.teachername = teachername;
    }
}
