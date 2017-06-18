package com.example.jun.mycapstone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

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

    public DrawSurfaceView2(Context context, AttributeSet set) {
        super(context, set);

        mContext = context;

        mPaint.setColor(Color.GREEN);
        mPaint.setTextSize(40);//
        mPaint.setStrokeWidth(DpiUtils.getPxFromDpi(getContext(), 2));
        mPaint.setAntiAlias(true);

        mRadar = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar);

        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("onSizeChanged", "in here w=" + w + " h=" + h);
        screenWidth = (double) w;
        screenHeight = (double) h;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mRadar, 0, 0, mPaint);

        int radarCentreX = mRadar.getWidth() / 2;
        int radarCentreY = mRadar.getHeight() / 2;

        screenPoint.clear();
        imagesPoint.clear();

        for (int i = 0; i < mBlips.length; i++) {
            Bitmap blip = mBlips[i];
            spot = mSpots[i];
            u = props.get(i);
            double dist = distInMetres(me, u);

            if (blip == null || spot == null)
                continue;

            if(dist > 70)
                dist = 70;

            double angle = bearing(me.latitude, me.longitude, u.latitude, u.longitude) - OFFSET;
            double xPos, yPos;

            if(angle < 0)
                angle = (angle+360)%360;

            xPos = Math.sin(Math.toRadians(angle)) * dist;
            yPos = Math.sqrt(Math.pow(dist, 2) - Math.pow(xPos, 2));

            if (angle > 90 && angle < 270) {
                yPos *= -1;
            }

            double posInPx = angle * (screenWidth / 90d);

            int blipCentreX = blip.getWidth() / 2;
            int blipCentreY = blip.getHeight() / 2;

            xPos = xPos - blipCentreX;
            yPos = yPos + blipCentreY;
            canvas.drawBitmap(blip, (radarCentreX + (int) xPos), (radarCentreY - (int) yPos), mPaint); //radar blip

            //reuse xPos
            int spotCentreX = spot.getWidth() / 2;
            int spotCentreY = spot.getHeight() / 2;
            xPos = posInPx - spotCentreX;

            if (angle <= 45)
                u.x = (float) ((screenWidth / 2) + xPos);

            else if (angle >= 315)
                u.x = (float) ((screenWidth / 2) - ((screenWidth*4) - xPos));

            else
                u.x = (float) (float)(screenWidth*9);

            screenPoint.add(u);
            imagesPoint.add(spot);

            u.y = (float)screenHeight/2 + spotCentreY;
            canvas.drawBitmap(spot, u.x, u.y, mPaint);
            canvas.drawText(u.description+"/-"+(long)u.nowDis+"m", u.x, u.y, mPaint);
        }
    }

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
