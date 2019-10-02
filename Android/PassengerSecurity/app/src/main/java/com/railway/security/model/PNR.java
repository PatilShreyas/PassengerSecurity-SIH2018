package com.railway.security.model;

/**
 * Created by HARSHALI PATIL on 09/03/2018.
 */

public class PNR {


    public String trainName;
    public String trainNo;
    public String pnrNo;
    public String srcStn;
    public String destStn;
    public String seatDetails;

    public PNR(String trainName, String trainNo, String pnrNo, String srcStn, String destStn, String seatDetails) {
        this.trainName = trainName;
        this.trainNo = trainNo;
        this.pnrNo = pnrNo;
        this.srcStn = srcStn;
        this.destStn = destStn;
        this.seatDetails = seatDetails;
    }
    public PNR(){

    }
}

