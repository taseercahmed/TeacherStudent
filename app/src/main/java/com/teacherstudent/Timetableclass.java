package com.teacherstudent;

public class Timetableclass {
    String className;
    String sectionName;
    String studyGroup;
    String subjectName;
    String assignedTeacher;
    String endTime;
    String roomNo;
    String startTime;

    public Timetableclass(String skey, String teachername, String lasttime, String room, String starttime) {
        this.assignedTeacher = teachername;
        this.endTime = lasttime;
        this.roomNo = room;
        this.startTime =starttime;
        this.subjectName=skey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    public Timetableclass(String className, String sectionName, String studyGroup, String subjectName, String assignedTeacher, String endTime, String roomNo, String startTime, String key) {
        this.key=key;
        this.className = className;
        this.sectionName = sectionName;
        this.studyGroup = studyGroup;
        this.subjectName = subjectName;
        this.assignedTeacher = assignedTeacher;
        this.endTime = endTime;
        this.roomNo = roomNo;
        this.startTime = startTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(String studyGroup) {
        this.studyGroup = studyGroup;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAssignedTeacher() {
        return assignedTeacher;
    }

    public void setAssignedTeacher(String assignedTeacher) {
        this.assignedTeacher = assignedTeacher;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
