package com.ppg.bpmeasure.Details;

public class Measurement {
    public int heartRate;
    public int systolic;
    public int diastolic;
public long timestamp;

    public Measurement(){

    }
    public Measurement(int heartRate, int systolic, int diastolic, long timestamp){
        this.heartRate = heartRate;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.timestamp= timestamp;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getSistolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
