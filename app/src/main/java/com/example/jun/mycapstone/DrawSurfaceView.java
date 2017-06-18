package com.example.jun.mycapstone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.jun.atest.R;

import java.util.ArrayList;

public class DrawSurfaceView extends View{
    Point me = new Point(NavigationActivity_1.lat, NavigationActivity_1.lon, "Me",0);
    Paint mPaint = new Paint();
    private double OFFSET = 0d;
    private double screenWidth, screenHeight = 0d;
    private Bitmap[] mSpots, mBlips;
    private Bitmap mRadar;
    static String C_point="Orange";

    static ArrayList<String> PointXsize = NavigationActivity_1.Point_X;
    static ArrayList<String> PointYsize = NavigationActivity_1.Point_Y;
    static ArrayList<String> PointZsize = NavigationActivity_1.Point_Y;

    double navi_lat, navi_lon;
    static double dis1;
    static double[] x;
    static double[] y;
    static double[] z;
    static int dc = 0;
    static int dc_number = 0;
    static double[] turn;

    static int checkFinish = 0;


    // turntype x좌표 형변환
    public static double[] change_x(ArrayList<String> strings)
    {
        double[] mx = new double[NavigationActivity_1.PointX().size()];

        for (int i=0; i < mx.length; i++)
        {
            mx[i] = Double.parseDouble(NavigationActivity_1.Point_X.get(i));
        }
        return mx;
    }
    // turntype y좌표 형변환
    public static double[] change_y(ArrayList<String> strings)
    {
        double[] my = new double[NavigationActivity_1.PointY().size()];

        for (int i=0; i < my.length; i++)
        {
            my[i] = Double.parseDouble(NavigationActivity_1.Point_Y.get(i));
        }
        return my;
    }
    // turntype 형변환
    public static double[] change_turn(ArrayList<String> strings)
    {
        double[] turn_1 = new double[NavigationActivity_1.PointY().size()];

        for (int i=0; i < turn_1.length; i++)
        {
            turn_1[i] = Double.parseDouble(NavigationActivity_1.turnType.get(i));
        }
        return turn_1;
    }

    //거리계산
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


    public static ArrayList<Point> props = new ArrayList<Point>();

    public DrawSurfaceView(Context c, Paint paint) {
        super(c);
    }

    public void point_mSpots_go(int dc){
        if(C_point.equals("Blue")) {
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.go_b);
        } else if(C_point.equals("Green")){
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.go_g);
        } else {
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.go);
        }
    }
    public void point_mSpots_left(int dc){
        if(C_point.equals("Blue")) {
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.left_b);
        } else if(C_point.equals("Green")){
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.left_g);
        } else {
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        }
    }
    public void point_mSpots_right(int dc){
        if(C_point.equals("Blue")) {
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.right_b);
        } else if(C_point.equals("Green")){
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.right_g);
        } else {
            mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        }
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){

            navi_lat = NavigationActivity_1.lat;
            navi_lon = NavigationActivity_1.lon;

            x = change_x(PointXsize);
            y = change_y(PointYsize);
            z = change_turn(PointZsize);

            if(dc < NavigationActivity_1.j) {
                for(int j=0; j < NavigationActivity_1.j; j++) {
                    props.add(new Point(y[j], x[j], NavigationActivity_1.MyTarget(),0));
                }

                dis1 = calDistance(navi_lat, navi_lon, y[dc], x[dc]);

                if(dis1 <= 100) {
                    mSpots = new Bitmap[props.size()];

                    if (turn[dc] == 200) {
                        mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
                        dc_number = 0;
                    } else if (turn[dc] == 201) {
                        mSpots[dc] = BitmapFactory.decodeResource(getResources(), R.drawable.des);
                        checkFinish = 1;
                        dc_number = 4;
                    } else if (turn[dc] == 12 || turn[dc] == 16 || turn[dc] == 17 || turn[dc] == 44 || turn[dc] == 118) {
                        point_mSpots_left(dc);
                        dc_number = 2;
                    } else if (turn[dc] == 13 || turn[dc] == 18 || turn[dc] == 19 || turn[dc] == 43 || turn[dc] == 117) {
                        point_mSpots_right(dc);
                        dc_number = 3;
                    } else if(turn[dc] == 11 || turn[dc] == 51){
                        point_mSpots_go(dc);
                        dc_number = 1;
                    }
                } else if(dis1 > 100) {
                    mSpots = new Bitmap[props.size()];
                    point_mSpots_go(dc);
                    dc_number = 1;
                }
                if (dis1 < 60) {
                    dc++;
                }
            }

            mBlips = new Bitmap[props.size()];
            for (int i = 0; i < mBlips.length; i++){
                mBlips[i] = BitmapFactory.decodeResource(getResources(), R.drawable.blip);
            }

            mHandler.sendEmptyMessageDelayed(0 ,3000);

            if(NavigationActivity_2_StartNavi.finish_navi == true){
                NavigationActivity_2_StartNavi.finish_navi = false;
                dc = 0;
                NavigationActivity_1.j = 0;
                mHandler.removeMessages(0);
            }
        }
    };

    public DrawSurfaceView(Context context, AttributeSet set) {
        super(context, set);
        mPaint.setColor(Color.GREEN);
        mPaint.setTextSize(70);
        mPaint.setStrokeWidth(DpiUtils.getPxFromDpi(getContext(), 1));
        mPaint.setAntiAlias(true);

        mRadar = BitmapFactory.decodeResource(context.getResources(), R.drawable.radar);

        // turnType 형변환
        for(int j=0; j<NavigationActivity_1.turnType.size();j++) {
            turn = change_turn(NavigationActivity_1.turnType);
        }

        final DBManager dbManager = new DBManager(context, "OptionInfo.db", null, 1);

        if(dbManager.Orange_select().equals("Orange")){
            C_point="Orange";
        } else if(dbManager.Blue_select().equals("Blue")){
            C_point="Blue";
        } else if(dbManager.Green_select().equals("Green")){
            C_point="Green";
        }

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

        for (int i = 0; i < mBlips.length; i++) {
            Bitmap blip = mBlips[i];
            Bitmap spot = mSpots[i];
            Point u = props.get(i);
            double dist = distInMetres(me, u);

            if (blip == null || spot == null)
                continue;

            if(dist > 70)
                dist = 70; // we have set points very far away for demonstration

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
                u.x = (float) (float)(screenWidth*9); //somewhere off the screen

            u.y = (float)screenHeight/2 + spotCentreY;
            canvas.drawBitmap(spot, u.x, u.y, mPaint); //camera spot
            canvas.drawText(u.description, u.x, u.y, mPaint); //text
        }
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
        double lnavi_lat = Math.toRadians(lat1);
        double la2 = Math.toRadians(lat2);
        double y = Math.sin(longDiff) * Math.cos(la2);
        double x = Math.cos(lnavi_lat) * Math.sin(la2) - Math.sin(lnavi_lat) * Math.cos(la2) * Math.cos(longDiff);

        double result = Math.toDegrees(Math.atan2(y, x));
        return (result+360.0d)%360.0d;
    }
}