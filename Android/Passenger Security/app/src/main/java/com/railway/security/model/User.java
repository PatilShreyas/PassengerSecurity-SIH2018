package com.railway.security.model;

import java.util.List;

/**
 * Created by HARSHALI PATIL on 09/03/2018.
 */

public class User {
    public String uid;
    public String name;
    public String phoneNumber;
    public String aadhaarNo;
    public String fcmToken;
    public List<String> trusty;
    public List<String> otherAlerts;

    public User(String uid, String name, String phoneNumber, String aadhaarNo, String fcmToken) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.aadhaarNo = aadhaarNo;
        this.fcmToken = fcmToken;
    }

    public User(){

    }

    public void setTrustyPersons(List<String> trusty){
        this.trusty = trusty;
    }
}
