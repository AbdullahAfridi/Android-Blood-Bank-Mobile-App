package com.rishabh.bloodbank.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donor {

    @SerializedName("name1")
    @Expose
    private String name1;
    @SerializedName("number1")
    @Expose
    private String number1;
    @SerializedName("city1")
    @Expose
    private String city1;

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getCity1() {
        return city1;
    }

    public void setCity1(String city1) {
        this.city1 = city1;
    }

}