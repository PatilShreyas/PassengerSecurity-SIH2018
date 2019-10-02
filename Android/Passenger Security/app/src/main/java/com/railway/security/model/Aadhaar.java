package com.railway.security.model;

/**
 * Created by HARSHALI PATIL on 09/03/2018.
 */

public class Aadhaar {

    public String uidNo;
    public String name;
    public String address;
    public String mobNo;
    public String dob;

    public Aadhaar(String uidNo, String name, String address, String mobNo, String dob) {
        this.uidNo = uidNo;
        this.name = name;
        this.address = address;
        this.mobNo = mobNo;
        this.dob = dob;
    }
    public Aadhaar(){

    }
}
