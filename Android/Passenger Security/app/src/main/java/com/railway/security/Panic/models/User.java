package com.railway.security.Panic.models;

import java.util.List;

/**
 * Created by HARSHALI PATIL on 21/12/2017.
 */

public class User {
    public Alert alert;
    public String uid;
    public String phoneNumber;
    public String name;
    public String fcmToken;
    public List<String> trusty;
    public List<String> otherAlerts;
    public String picUrl;

    User(){}

    public User(String uid, String name, String phoneNumber, String fcmToken){
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.fcmToken = fcmToken;
    }
    public void setTrustyPersons(List<String> trusty){
        this.trusty = trusty;
    }
}
