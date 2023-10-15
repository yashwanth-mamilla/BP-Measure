package com.ppg.bpmeasure.Details;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

public class UserInfo implements Serializable {
    public static int MALE=1;
    public static int FEMALE=0;
    public String name;
    public String email;
    public String password;
    public Double height;
    public Double weight;
    public Double age;
    public int gender;
    public Double ejectionTime;
    public Double ROB;
    public Date timestampDate;

    public UserInfo() {
    }

    public UserInfo(String name, String email, String password,Double height,Double weight,Double age,int gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.height=height;
        this.weight=weight;
        this.age=age;
        this.gender=gender;
        this.ejectionTime=Double.valueOf(0);
        this.ROB=Double.valueOf(0);
    }

}
