package com.miproducts.miwatch.Container;

/**
 * This will store
 * 1. zipcode of a location the user wanted to find the weather of.
 * 2. State and city of a location the user wanted to find the weather of.
 * 3. last temperature the user retrieved from a weather fetch.
 * Created by ladam_000 on 8/1/2015.
 */
public class WeatherLocation {
    private int temperature;
    private String zipcode;
    private String city;
    private String desc;



    private String state;
    private long time_stamp;




    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }



    public long getTime_stamp() {
        return time_stamp;
    }

    public String getDesc() {
        return desc;
    }



    public WeatherLocation() {

    }

    public int getTemperature() {
        return temperature;
    }

    public String getZipcode() {
        return zipcode;
    }


    public String getCity() {
        return city;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public WeatherLocation(int temperature, String zipcode, String city) {
        this.temperature = temperature;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
