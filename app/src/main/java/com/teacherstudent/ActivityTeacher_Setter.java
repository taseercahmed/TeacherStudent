package com.teacherstudent;

public class ActivityTeacher_Setter {
    String eventDate,eventTitle,eventDiscription,eventStartTime,eventEndTime;

    public ActivityTeacher_Setter(String eventDate, String eventTitle, String eventDiscription, String eventStartTime, String eventEndTime) {
        this.eventDate = eventDate;
        this.eventTitle = eventTitle;
        this.eventDiscription = eventDiscription;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDiscription() {
        return eventDiscription;
    }

    public void setEventDiscription(String eventDiscription) {
        this.eventDiscription = eventDiscription;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }
}
