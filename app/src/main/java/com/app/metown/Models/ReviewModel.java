package com.app.metown.Models;

public class ReviewModel {

    String ToUsersProfilePicture, ToUsersNickName, Notes;

    public String getToUsersProfilePicture() {
        return ToUsersProfilePicture;
    }

    public void setToUsersProfilePicture(String toUsersProfilePicture) {
        ToUsersProfilePicture = toUsersProfilePicture;
    }

    public String getToUsersNickName() {
        return ToUsersNickName;
    }

    public void setToUsersNickName(String toUsersNickName) {
        ToUsersNickName = toUsersNickName;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}