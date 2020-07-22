package com.teacherstudent;

public class ClassSectionGroup {
    String className, sectionName, studyGroup;

    public ClassSectionGroup(String className, String sectionName, String studyGroup) {
        this.className = className;
        this.sectionName = sectionName;
        this.studyGroup = studyGroup;
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
}
