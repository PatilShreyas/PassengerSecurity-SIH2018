package com.railway.security.model;

/**
 * Created by HARSHALI PATIL on 09/03/2018.
 */

public class FIR {

    public String firNo;
    public String name;
    public String mobNo;
    public String aadhaarNo;
    public String uid;
    public String pnrNo;
    public String trainNo;
    public String lastStn;
    public String crime;
    public String info;
    public String time;
    public String status;

    public FIR(String firNo, String name, String mobNo, String aadhaarNo, String uid, String pnrNo, String trainNo, String lastStn, String crime, String info) {
        this.firNo = firNo;
        this.name = name;
        this.mobNo = mobNo;
        this.aadhaarNo = aadhaarNo;
        this.uid = uid;
        this.pnrNo = pnrNo;
        this.trainNo = trainNo;
        this.lastStn = lastStn;
        this.crime = crime;
        this.info = info;
    }
    public FIR(){

    }
}
