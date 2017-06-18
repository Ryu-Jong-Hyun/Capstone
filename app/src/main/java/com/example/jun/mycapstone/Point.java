package com.example.jun.atest;

public class Point{

    public double longitude = 0f;
    public double latitude = 0f;
    public String description;
    public double nowDis;
    public float x, y = 0;

    public Point(double lat, double lon, String desc, double nDis) {
        this.latitude = lat;
        this.longitude = lon;
        this.description = desc;
        this.nowDis = nDis;
    }

    public String getDescription() {
        return description;
    }


    public double getNowDis() {
        return nowDis;
    }
}
