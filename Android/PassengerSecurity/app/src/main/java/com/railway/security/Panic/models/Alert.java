package com.railway.security.Panic.models;

/**
 * Created by HARSHALI PATIL on 22/12/2017.
 */

public class Alert {
    public boolean status;
    public String uid;
    public String name;
    public String phoneNumber;
    public String message;
    public String time;
    public double latitude, longitude;
    public String voiceUrl;
    public String picUrl;

    public Alert(){}
    public Alert(boolean status, String uid, String phoneNumber, double latitude, double longitude, String time){
        this.status = status;
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = "I'm in Danger";
        this.time = time;
    }
}
