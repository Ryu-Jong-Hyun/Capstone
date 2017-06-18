package com.example.jun.atest;

/**
 * Created by jun on 2017-03-05.
 */


public class Poi {

    private int id;
    private String name;
    private String telNo;

    private Double frontLat;
    private Double frontLon;
    private Double noorLon;
    private Double noorLat;

    private String upperAddrName;
    private String middleAddrName;
    private String lowerAddrName;
    private String detailAddrName;
    private String firstNo;
    private String secondNo;
    private String roadName;
    private String firstBuildNo;
    private String secondBuildNo;
    private String radius;
    private String bizName;
    private String upperBizName;
    private String middleBizName;
    private String lowerBizName;
    private String detailBizName;
    private String rpFlag;
    private int parkFlag;
    private int detailInfoFlag;
    private String desc;

    @Override
    public String toString() {
        return "Poi [id=" + id + ", name=" + name + ", telNo=" + telNo + ", frontLat=" + frontLat + ", frontLon="
                + frontLon + ", noorLon=" + noorLon + ", noorLat=" + noorLat + ", upperAddrName=" + upperAddrName
                + ", middleAddrName=" + middleAddrName + ", lowerAddrName=" + lowerAddrName + ", detailAddrName="
                + detailAddrName + ", firstNo=" + firstNo + ", secondNo=" + secondNo + ", radius=" + radius
                + ", bizName=" + bizName + ", upperBizName=" + upperBizName + ", middleBizName=" + middleBizName
                + ", lowerBizName=" + lowerBizName + ", detailBizName=" + detailBizName + ", rpFlag=" + rpFlag
                + ", parkFlag=" + parkFlag + ", detailInfoFlag=" + detailInfoFlag + ", desc=" + desc + "]";
    }


    public String getRoadName() {
        return roadName;
    }


    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }


    public String getFirstBuildNo() {
        return firstBuildNo;
    }


    public void setFirstBuildNo(String firstBuildNo) {
        this.firstBuildNo = firstBuildNo;
    }


    public String getSecondBuildNo() {
        return secondBuildNo;
    }


    public void setSecondBuildNo(String secondBuildNo) {
        this.secondBuildNo = secondBuildNo;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTelNo() {
        return telNo;
    }
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    public Double getFrontLat() {
        return frontLat;
    }
    public void setFrontLat(Double frontLat) {
        this.frontLat = frontLat;
    }
    public Double getFrontLon() {
        return frontLon;
    }
    public void setFrontLon(Double frontLon) {
        this.frontLon = frontLon;
    }
    public Double getNoorLon() {
        return noorLon;
    }
    public void setNoorLon(Double noorLon) {
        this.noorLon = noorLon;
    }

    public Double getNoorLat() {
        return noorLat;
    }
    public void setNoorLat(Double noorLat) {
        this.noorLat = noorLat;
    }
    public String getUpperAddrName() {
        return upperAddrName;
    }
    public void setUpperAddrName(String upperAddrName) {
        this.upperAddrName = upperAddrName;
    }
    public String getMiddleAddrName() {
        return middleAddrName;
    }
    public void setMiddleAddrName(String middleAddrName) {
        this.middleAddrName = middleAddrName;
    }
    public String getLowerAddrName() {
        return lowerAddrName;
    }
    public void setLowerAddrName(String lowerAddrName) {
        this.lowerAddrName = lowerAddrName;
    }
    public String getDetailAddrName() {
        return detailAddrName;
    }
    public void setDetailAddrName(String detailAddrName) {
        this.detailAddrName = detailAddrName;
    }
    public String getFirstNo() {
        return firstNo;
    }
    public void setFirstNo(String firstNo) {
        this.firstNo = firstNo;
    }
    public String getSecondNo() {
        return secondNo;
    }
    public void setSecondNo(String secondNo) {
        this.secondNo = secondNo;
    }
    public String getRadius() {
        return radius;
    }
    public void setRadius(String radius) {
        this.radius = radius;
    }
    public String getBizName() {
        return bizName;
    }
    public void setBizName(String bizName) {
        this.bizName = bizName;
    }
    public String getUpperBizName() {
        return upperBizName;
    }
    public void setUpperBizName(String upperBizName) {
        this.upperBizName = upperBizName;
    }
    public String getMiddleBizName() {
        return middleBizName;
    }
    public void setMiddleBizName(String middleBizName) {
        this.middleBizName = middleBizName;
    }
    public String getLowerBizName() {
        return lowerBizName;
    }
    public void setLowerBizName(String lowerBizName) {
        this.lowerBizName = lowerBizName;
    }
    public String getDetailBizName() {
        return detailBizName;
    }
    public void setDetailBizName(String detailBizName) {
        this.detailBizName = detailBizName;
    }
    public String getRpFlag() {
        return rpFlag;
    }
    public void setRpFlag(String rpFlag) {
        this.rpFlag = rpFlag;
    }
    public int getParkFlag() {
        return parkFlag;
    }
    public void setParkFlag(int parkFlag) {
        this.parkFlag = parkFlag;
    }
    public int getDetailInfoFlag() {
        return detailInfoFlag;
    }
    public void setDetailInfoFlag(int detailInfoFlag) {
        this.detailInfoFlag = detailInfoFlag;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

}