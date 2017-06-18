package com.example.jun.mycapstone;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jun on 2017-03-05.
 */

public class TMapRouteCreater {

    public SearchPoiInfo requestInfo(String Target) {
        try {

            String result = "";
            URL url = new URL("https://apis.skplanetx.com/tmap/pois?version=1&page=1&count=1&searchKeyword="+Target+"&areaLLCode=&areaLMCode=&resCoordType=WGS84GEO&searchType=all&searchtypCd=A&radius=0&reqCoordType=WGS84GEO&centerLon=&centerLat=&multiPoint=Y&callback=");

            URLConnection con= url.openConnection();

            con.setRequestProperty("x-skpop-userId","kjh2380");
            con.setRequestProperty("Accept-Language","ko_KR");
            con.setRequestProperty("Date", new Date().toString());
            con.setRequestProperty("Accept","application/json");
            con.setRequestProperty("appKey", "a6c0cc4b-1fe2-369f-9daa-3edcfec32698");

            InputStream ins=con.getInputStream();
            Scanner scan=new Scanner(ins);

            while(scan.hasNext()){
                String str=scan.nextLine();
                System.out.println(str);
                result+=str;
            }
            scan.close();

            //  System.out.println(result);

            //System.out.println("T_Map에서 가져온 좌표정보를 GSON을 사용해 JSON형식 데이터를 파싱한 결과:");

            Type typeOfHashMap = new TypeToken<Map<String, SearchPoiInfo>>() { }.getType();
            Map<String, SearchPoiInfo> newMap = new Gson().fromJson(result, typeOfHashMap);

            SearchPoiInfo info=newMap.get("searchPoiInfo");

            // num1 = info.getPois().get("poi").get(0).getNoorLat();
            //num2 = info.getPois().get("poi").get(0).getNoorLon();

            return  info;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
