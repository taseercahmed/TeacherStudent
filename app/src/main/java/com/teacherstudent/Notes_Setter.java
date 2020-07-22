package com.teacherstudent;

public class Notes_Setter {
    String documentDate,documentMessage,documentSenderEmail,documentTime,documentType,subjectName;

    public Notes_Setter(String documentDate, String documentMessage, String documentSenderEmail, String documentTime, String documentType, String subjectName) {
        this.documentDate = documentDate;
        this.documentMessage = documentMessage;
        this.documentSenderEmail = documentSenderEmail;
        this.documentTime = documentTime;
        this.documentType = documentType;
        this.subjectName = subjectName;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentMessage() {
        return documentMessage;
    }

    public void setDocumentMessage(String documentMessage) {
        this.documentMessage = documentMessage;
    }

    public String getDocumentSenderEmail() {
        return documentSenderEmail;
    }

    public void setDocumentSenderEmail(String documentSenderEmail) {
        this.documentSenderEmail = documentSenderEmail;
    }

    public String getDocumentTime() {
        return documentTime;
    }

    public void setDocumentTime(String documentTime) {
        this.documentTime = documentTime;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
