package com.example.jun.mycapstone;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jun on 2017-06-18.
 */

public class DrawSurfaceView2 {

    public class TMapBuildingCreater {
        public SearchPoiInfo requestInfo(String BName) {
            try {
                String result = "";
                URL url = new URL("https://apis.skplanetx.com/tmap/pois?version=1&page=1&count=1&searchKeyword=" + BName + "&areaLLCode=&areaLMCode=&resCoordType=WGS84GEO&searchType=all&searchtypCd=A&radius=0&reqCoordType=WGS84GEO&centerLon=&centerLat=&multiPoint=Y&callback=");

                URLConnection con = url.openConnection();

                con.setRequestProperty("x-skpop-userId", "kjh2380");
                con.setRequestProperty("Accept-Language", "ko_KR");
                con.setRequestProperty("Date", new Date().toString());
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("appKey", "a6c0cc4b-1fe2-369f-9daa-3edcfec32698");

                InputStream ins = con.getInputStream();
                Scanner scan = new Scanner(ins);

                while (scan.hasNext()) {
                    String str = scan.nextLine();
                    System.out.println(str);
                    result += str;
                }
                scan.close();

                Type typeOfHashMap = new TypeToken<Map<String, SearchPoiInfo>>() {
                }.getType();
                Map<String, SearchPoiInfo> newMap = new Gson().fromJson(result, typeOfHashMap);
                SearchPoiInfo info = newMap.get("searchPoiInfo");
                return info;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class TMapBuildingCreater2{
        public SearchPoiInfo requestInfo(String BName) {
            try {

                String result = "";
                URL url = new URL("https://apis.skplanetx.com/tmap/pois?version=1&page=2&count=1&searchKeyword=" + BName + "&areaLLCode=&areaLMCode=&resCoordType=WGS84GEO&searchType=all&searchtypCd=A&radius=0&reqCoordType=WGS84GEO&centerLon=&centerLat=&multiPoint=Y&callback=");

                URLConnection con = url.openConnection();

                con.setRequestProperty("x-skpop-userId", "kjh2380");
                con.setRequestProperty("Accept-Language", "ko_KR");
                con.setRequestProperty("Date", new Date().toString());
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("appKey", "a6c0cc4b-1fe2-369f-9daa-3edcfec32698");

                InputStream ins = con.getInputStream();
                Scanner scan = new Scanner(ins);

                while (scan.hasNext()) {
                    String str = scan.nextLine();
                    System.out.println(str);
                    result += str;
                }
                scan.close();

                Type typeOfHashMap = new TypeToken<Map<String, SearchPoiInfo>>() {
                }.getType();
                Map<String, SearchPoiInfo> newMap = new Gson().fromJson(result, typeOfHashMap);

                SearchPoiInfo info = newMap.get("searchPoiInfo");
                return info;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class TMapBuildingCreater3{
        public SearchPoiInfo requestInfo(String BName) {
            try {
                String result = "";
                URL url = new URL("https://apis.skplanetx.com/tmap/pois?version=1&page=3&count=1&searchKeyword=" + BName + "&areaLLCode=&areaLMCode=&resCoordType=WGS84GEO&searchType=all&searchtypCd=A&radius=0&reqCoordType=WGS84GEO&centerLon=&centerLat=&multiPoint=Y&callback=");

                URLConnection con = url.openConnection();

                con.setRequestProperty("x-skpop-userId", "kjh2380");
                con.setRequestProperty("Accept-Language", "ko_KR");
                con.setRequestProperty("Date", new Date().toString());
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("appKey", "a6c0cc4b-1fe2-369f-9daa-3edcfec32698");

                InputStream ins = con.getInputStream();
                Scanner scan = new Scanner(ins);

                while (scan.hasNext()) {
                    String str = scan.nextLine();
                    System.out.println(str);
                    result += str;
                }
                scan.close();

                Type typeOfHashMap = new TypeToken<Map<String, SearchPoiInfo>>() {
                }.getType();
                Map<String, SearchPoiInfo> newMap = new Gson().fromJson(result, typeOfHashMap);


                SearchPoiInfo info = newMap.get("searchPoiInfo");

                return info;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
