package com.app.metown.Models;

public class NotificationModel {

    String NotificationID, NotificationName;

    public NotificationModel(String NotificationID, String NotificationName) {
        this.NotificationID = NotificationID;
        this.NotificationName = NotificationName;
    }

    public String getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public String getNotificationName() {
        return NotificationName;
    }

    public void setNotificationName(String notificationName) {
        NotificationName = notificationName;
    }
}
