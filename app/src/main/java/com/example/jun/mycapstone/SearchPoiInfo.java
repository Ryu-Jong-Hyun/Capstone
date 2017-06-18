package com.example.jun.atest;

/**
 * Created by jun on 2017-03-05.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchPoiInfo {

    private int totalCount;
    private int count;
    private int page;

    private HashMap<String, List<com.example.jun.atest.Poi>> pois=new HashMap<String, List<com.example.jun.atest.Poi>>();

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public HashMap<String, List<com.example.jun.atest.Poi>> getPois() {
        return pois;
    }

    public void setPois(HashMap<String, List<com.example.jun.atest.Poi>> pois) {
        this.pois = pois;
    }

    @Override
    public String toString() {
        return "SearchPoiInfo [totalCount=" + totalCount + ", count=" + count + ", page=" + page + ", pois=" + pois
                + "]";
    }











}