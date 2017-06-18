package com.example.jun.mycapstone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jun.atest.R;
import com.skp.Tmap.TMapGpsManager;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    static double Lat, Lon;
    static final int GET_STRING = 1;

    AlertDialog alertDialog;

    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int pmCheckLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int pmCheckCam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pmCheckLoc == PackageManager.PERMISSION_DENIED && pmCheckCam == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            }
        } else {
            buttonClick();
        }

        if(pmCheckLoc == PackageManager.PERMISSION_GRANTED && pmCheckCam == PackageManager.PERMISSION_GRANTED) {
            buttonClick();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                buttonClick();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},2000);
            }
        }
    }

    public void buttonClick() {
        TMapGpsManager tGps = new TMapGpsManager(MainActivity.this);
        tGps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        tGps.setMinTime(0);
        tGps.setMinDistance(0);
        tGps.OpenGps();

        Button Navibutton = (Button) findViewById(R.id.button_Navigation);
        Button MapViewbutton = (Button) findViewById(R.id.button_MapView);
        Button BookMarkbutton = (Button) findViewById(R.id.button_BookMark);
        Button Optionbutton = (Button) findViewById(R.id.button_Option);

        //네이게이션 버튼 이동
        Navibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawSurfaceView.props.clear();
                Intent intent = new Intent(MainActivity.this, NavigationActivity_1.class);
                startActivity(intent);
            }
        });

        //맵뷰 버튼 이동
        MapViewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                Intent intent = new Intent(MainActivity.this, MapViewActivity_1.class);
                startActivityForResult(intent, GET_STRING);
            }
        });

        //북마크 버튼 이동
        BookMarkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
                startActivity(intent);
            }
        });

        // 옵션 버튼 이동
        Optionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(intent);
            }
        });

    }

        @Override
        public void onLocationChange (Location location){
            Lat = location.getLatitude();
            Lon = location.getLongitude();
        }

        @Override
        public void onBackPressed () {
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
                finish();
                return;
            }
            Toast.makeText(this, "뒤로 버튼을 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            lastTimeBackPressed = System.currentTimeMillis();
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GET_STRING) {
                alertDialog.dismiss();
            }
        }
    }


