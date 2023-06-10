package com.techno71.fireservice.Model;

public class MyLocation {
    private Object latitude ,longitude , accessToken , phone;

    public MyLocation(Object latitude, Object longitude, Object accessToken, Object phone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accessToken = accessToken;
        this.phone = phone;
    }

    public MyLocation() {
    }

    public Object getLatitude() {
        return latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public Object getAccessToken() {
        return accessToken;
    }

    public Object getPhone() {
        return phone;
    }
}
