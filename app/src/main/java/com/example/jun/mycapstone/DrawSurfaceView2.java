package com.example.jun.mycapstone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.jun.atest.R;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class DrawSurfaceView2 extends View {
    Point me = new Point(MainActivity.Lat, MainActivity.Lon, "Me", 0);
    Paint mPaint = new Paint();

    private Context mContext;

    private double OFFSET = 0d;
    private double screenWidth, screenHeight = 0d;

    private Bitmap[] mSpots, mBlips;
    private Bitmap mRadar;

    private ArrayList<Point> screenPoint = new ArrayList<>();
    private ArrayList<Bitmap> imagesPoint = new ArrayList<>();

    public static ArrayList<Point> props = new ArrayList<Point>();

    Bitmap spot;
    Point u;

    String BuildingInfo;
    String BuildingInfo2;
    String BuildingInfo3;

    static int cBuilding;
    static int resultCount;

    static double hereLat;
    static double hereLon;

    AlertDialog alertDialog;

    public DrawSurfaceView2(Context c, Paint paint) {
        super(c);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            cBuilding = 0;

            hereLat = MainActivity.Lat;
            hereLon = MainActivity.Lon;

            int num = MapViewActivity_1.rLength;
            double BDis[] = new double[num];

            for(int i = 0; i < MapViewActivity_1.rLength; i++) {
                BDis[i] = calDistance(hereLat, hereLon, MapViewActivity_1.lat[i], MapViewActivity_1.lon[i]);
            }

            for (int i = 0; i < MapViewActivity_1.rLength; i++) {
                props.add(new Point(MapViewActivity_1.lat[i], MapViewActivity_1.lon[i], MapViewActivity_1.bname[i], BDis[i]));
            }

            mSpots = new Bitmap[props.size()];
            for (int i = 0; i < MapViewActivity_1.rLength; i++) {
                if (BDis[i] < 200) {
                    if(MapViewActivity_1.kname[i].equals("편의시설")) {
                        mSpots[i] = BitmapFactory.decodeResource(getResources(), R.drawable.building);
                    } else if(MapViewActivity_1.kname[i].equals("학교")) {
                        mSpots[i] = BitmapFactory.decodeResource(getResources(), R.drawable.school);
                    } else if(MapViewActivity_1.kname[i].equals("공공시설")){
                        mSpots[i] = BitmapFactory.decodeResource(getResources(), R.drawable.society);
                    }
                    cBuilding++;
                }
            }
            resultCount = cBuilding;

            mBlips = new Bitmap[props.size()];
            for (int i = 0; i < mBlips.length; i++){
                mBlips[i] = BitmapFactory.decodeResource(getResources(), R.drawable.blip);
            }

            mHandler.sendEmptyMessageDelayed(0 ,1000);
        }
    };

    public static double calDistance(double lat1, double lon1, double lat2, double lon2){
        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    private static double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }

    private static double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < screenPoint.size(); i++) {
                    if (imagesPoint.get(i) == null) {
                        continue;
                    }

                    if (event.getX() <= screenPoint.get(i).x + imagesPoint.get(i).getWidth() && event.getX() >= screenPoint.get(i).x && event.getY()
                            <= screenPoint.get(i).y + imagesPoint.get(i).getHeight() && event.getY() >= screenPoint.get(i).y) {

                        TMapBuildingCreater bCreater = new TMapBuildingCreater();
                        TMapBuildingCreater2 bCreater2 = new TMapBuildingCreater2();
                        TMapBuildingCreater3 bCreater3 = new TMapBuildingCreater3();

                        SearchPoiInfo info = bCreater.requestInfo(screenPoint.get(i).getDescription());
                        SearchPoiInfo info2 = bCreater2.requestInfo(screenPoint.get(i).getDescription());
                        SearchPoiInfo info3 = bCreater3.requestInfo(screenPoint.get(i).getDescription());

                        try {
                            BuildingInfo = info.getPois().get("poi").get(0).getName() + "\n\n전화번호: " + info.getPois().get("poi").get(0).getTelNo() + "\n\n주소: " + info.getPois().get("poi").get(0).getUpperAddrName()
                                    + " " + info.getPois().get("poi").get(0).getMiddleAddrName() + " " + info.getPois().get("poi").get(0).getLowerAddrName() + " " + info.getPois().get("poi").get(0).getFirstNo()
                                    + "-" + info.getPois().get("poi").get(0).getSecondNo() + "\n\n" + info.getPois().get("poi").get(0).getUpperBizName() + "/ " + info.getPois().get("poi").get(0).getMiddleBizName()
                                    + "/ " + info.getPois().get("poi").get(0).getLowerBizName() + "\n\n" + info.getPois().get("poi").get(0).getDesc();

                            try {
                                BuildingInfo2 = info2.getPois().get("poi").get(0).getName() + "\n\n전화번호: " + info2.getPois().get("poi").get(0).getTelNo() + "\n\n주소: " + info2.getPois().get("poi").get(0).getUpperAddrName()
                                        + " " + info2.getPois().get("poi").get(0).getMiddleAddrName() + " " + info2.getPois().get("poi").get(0).getLowerAddrName() + " " + info2.getPois().get("poi").get(0).getFirstNo()
                                        + "-" + info2.getPois().get("poi").get(0).getSecondNo() + "\n\n" + info2.getPois().get("poi").get(0).getUpperBizName() + "/ " + info2.getPois().get("poi").get(0).getMiddleBizName()
                                        + "/ " + info2.getPois().get("poi").get(0).getLowerBizName() + "\n\n" + info2.getPois().get("poi").get(0).getDesc();
                            } catch (IndexOutOfBoundsException e) {

                            }

                            try {
                                BuildingInfo3 = info3.getPois().get("poi").get(0).getName() + "\n\n전화번호: " + info3.getPois().get("poi").get(0).getTelNo() + "\n\n주소: " + info3.getPois().get("poi").get(0).getUpperAddrName()
                                        + " " + info3.getPois().get("poi").get(0).getMiddleAddrName() + " " + info3.getPois().get("poi").get(0).getLowerAddrName() + " " + info3.getPois().get("poi").get(0).getFirstNo()
                                        + "-" + info3.getPois().get("poi").get(0).getSecondNo() + "\n\n" + info3.getPois().get("poi").get(0).getUpperBizName() + "/ " + info3.getPois().get("poi").get(0).getMiddleBizName()
                                        + "/ " + info3.getPois().get("poi").get(0).getLowerBizName() + "\n\n" + info3.getPois().get("poi").get(0).getDesc();
                            } catch (IndexOutOfBoundsException e) {

                            }

                        } catch (NullPointerException e) {
                            Toast.makeText(mContext, "Loading Error Restart", Toast.LENGTH_SHORT).show();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, alertDialog.THEME_HOLO_LIGHT);
                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
                        alertDialog = builder.create();
                        adapter.add(info.getPois().get("poi").get(0).getName());

                        try {
                            adapter.add(info2.getPois().get("poi").get(0).getName());
                        } catch (IndexOutOfBoundsException e) {

                        }

                        try {
                            adapter.add(info3.getPois().get("poi").get(0).getName());
                        } catch (IndexOutOfBoundsException e) {

                        }

                        builder.setTitle("건물목록")
                                .setCancelable(false)
                                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });


                        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Binfo1 = BuildingInfo;
                                String Binfo2 = BuildingInfo2;
                                String Binfo3 = BuildingInfo3;

                                AlertDialog.Builder inBuilder = new AlertDialog.Builder(mContext, alertDialog.THEME_HOLO_LIGHT);

                                if(which == 0){
                                    inBuilder.setMessage(Binfo1);
                                } else if(which == 1){
                                    inBuilder.setMessage(Binfo2);
                                } else {
                                    inBuilder.setMessage(Binfo3);
                                }

                                inBuilder.setTitle("정보 보기")
                                        .setCancelable(false)
                                        .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert2 = inBuilder.create();
                                alert2.show();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {

                    }
                }
        }
        return super.onTouchEvent(event);
    }

    public void setOffset(float offset) {
        this.OFFSET = offset;
    }

    public void setMyLocation(double latitude, double longitude) {
        me.latitude = latitude;
        me.longitude = longitude;
    }

    protected double distInMetres(Point me, Point u) {

        double lat1 = me.latitude;
        double lng1 = me.longitude;

        double lat2 = u.latitude;
        double lng2 = u.longitude;

        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist * 1000;
    }

    protected static double bearing(double lat1, double lon1, double lat2, double lon2) {
        double longDiff = Math.toRadians(lon2 - lon1);
        double la1 = Math.toRadians(lat1);
        double la2 = Math.toRadians(lat2);
        double y = Math.sin(longDiff) * Math.cos(la2);
        double x = Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(longDiff);

        double result = Math.toDegrees(Math.atan2(y, x));
        return (result+360.0d)%360.0d;
    }
}
