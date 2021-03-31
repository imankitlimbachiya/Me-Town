package com.app.metown.Models;

public class LocationModel {

    String ID, LocationName, Latitude, Longitude, UserRange;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getUserRange() {
        return UserRange;
    }

    public void setUserRange(String userRange) {
        UserRange = userRange;
    }
}