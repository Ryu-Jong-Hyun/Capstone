package com.example.jun.mycapstone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jun.atest.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.jun.mycapstone.DrawSurfaceView.dc;

public class MapViewActivity_1 extends AppCompatActivity {
    DBManager DBOpen;
    SQLiteDatabase db;

    static int rLength;
    int cLength;

    static double lat[];

    static double lon[];
    static String bname[];
    static String kname[];

    private static ArrayList<Address> addressCoordinates=new ArrayList<Address>();
    private static ArrayList<String> address=new ArrayList<String>();
    private static ArrayList<String> building=new ArrayList<String>();
    private static ArrayList<String> kind=new ArrayList<String>();

    private static final String TAG = "Compass";
    private static boolean DEBUG = false;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private DrawSurfaceView2 mDrawView;
    LocationManager locMgr;

    private final SensorEventListener mListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (DEBUG)
                Log.d(TAG, "sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");
            if (mDrawView != null) {
                mDrawView.setOffset(event.values[0]);
                mDrawView.invalidate();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_map_view_1);

        DBOpen = new DBManager(this);
        db = DBOpen.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM BuildingInfo", null);
        c.moveToLast();
        rLength = c.getCount();
        cLength = c.getColumnCount();
        c.moveToFirst();

        lat = new double[rLength];
        lon = new double[rLength];
        bname = new String[rLength];
        kname = new String[rLength];

        final String[][] array = new String[rLength][cLength];

        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        for (int i = 0; i < rLength; i++) {
            for (int j = 0; j < cLength; j++) {
                array[i][j] = c.getString(j);
            }
            c.moveToNext();
        }

        for (int i = 0; i < rLength; i++) {
            address.add(i, array[i][1] + " " + array[i][2] + " " + array[i][3] + " " + array[i][4] + "-" + array[i][5]);
            building.add(i, array[i][6]);
            kind.add(i, array[i][7]);
        }

        for (int i = 0; i < rLength; i++) {
            bname[i] = building.get(i);
            kname[i] = kind.get(i);
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            for (int i = 0; i < rLength; i++) {
                List<Address> list = geocoder.getFromLocationName(address.get(i), 1);
                addressCoordinates.add(list.get(0));
            }
            for (int i = 0; i < rLength; i++) {
                Address addr = addressCoordinates.get(i);
                lat[i] = addr.getLatitude();
                lon[i] = addr.getLongitude();
            }
        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(MapViewActivity_1.this, "Error 발생", Toast.LENGTH_SHORT).show();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mDrawView = (DrawSurfaceView2) findViewById(R.id.drawSurfaceView);
        locMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        LocationProvider high = locMgr.getProvider(locMgr.getBestProvider(LocationUtils.createFineCriteria(), true));

        try {
            locMgr.requestLocationUpdates(high.getName(), 0, 0f, new LocationListener() {
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "Location Changed");

                    mDrawView.setMyLocation(location.getLatitude(), location.getLongitude());
                    mDrawView.invalidate();
                }

                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                public void onProviderEnabled(String s) {

                }

                public void onProviderDisabled(String s) {

                }
            });
        } catch (SecurityException e) {

        }

        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onResume() {
        if (DEBUG) {
            Log.d(TAG, "onResume");
        }
        super.onResume();
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        if (DEBUG)
            Log.d(TAG, "onStop");
        mSensorManager.unregisterListener(mListener);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            TextView bCount = (TextView)findViewById(R.id.buildingNum);

            bCount.setText("현재 반경 200m 안에 있는 건물은 "+DrawSurfaceView2.resultCount+"개 입니다.");

            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };
}
