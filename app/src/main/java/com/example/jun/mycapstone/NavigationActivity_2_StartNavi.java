package com.example.jun.mycapstone;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationActivity_2_StartNavi extends AppCompatActivity {


    //거리 계산 부분//
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            TextView endDis = (TextView) findViewById(R.id.endDis);
            TextView turnDis = (TextView) findViewById(R.id.turnTypeDis);
            endDis.setText("총:" + String.valueOf((long) NavigationActivity_1.allDis) + "m");
            turnDis.setText("안내:" + String.valueOf((long) DrawSurfaceView.dis1) + "m");

            if (OptionActivity.change_vibration.equals("vibe_on")) {
                vibe_sec = 1000;
            }

            if (OptionActivity.change_sound.equals("sound_on")) {
                voice_g = voice_g.create(NavigationActivity_2_StartNavi.this, R.raw.go);
                voice_left = voice_left.create(NavigationActivity_2_StartNavi.this, R.raw.left);
                voice_right = voice_right.create(NavigationActivity_2_StartNavi.this, R.raw.right);
                voice_finish = voice_finish.create(NavigationActivity_2_StartNavi.this, R.raw.destination);
            }

            if(dc_num == dc_count) {
                dc_count = dc;

                switch (DrawSurfaceView.dc_number) {
                    case 0:
                        Toast.makeText(NavigationActivity_2_StartNavi.this, "경로안내를 시작합니다.", Toast.LENGTH_SHORT).show();
                        vibe.vibrate(vibe_sec);
                        dc_num++;
                        break;
                    case 1:
                        voice_g.start();
                        vibe.vibrate(vibe_sec);
                        dc_num++;
                        break;
                    case 2:
                        voice_left.start();
                        vibe.vibrate(vibe_sec);
                        dc_num++;
                        break;
                    case 3:
                        voice_right.start();
                        vibe.vibrate(vibe_sec);
                        dc_num++;
                        break;
                    case 4:
                        voice_finish.start();
                        vibe.vibrate(vibe_sec);
                        dc_num++;
                        break;
                }
            }

            if (dc_count > NavigationActivity_1.j) {
                finish_navi = true;
                dc_num = 0;
            }

            if(DrawSurfaceView.checkFinish == 1){
                Toast.makeText(NavigationActivity_2_StartNavi.this, "목적지에 도착했습니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(NavigationActivity_2_StartNavi.this, "경로안내를 중단합니다.", Toast.LENGTH_SHORT).show();
            }

            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_2__start_navi);

        //intent로 좌표 받아오는 부분
        Intent intent = getIntent();
        target_lat = intent.getDoubleExtra("TargetLat", 0.0);
        target_lon = intent.getDoubleExtra("TargetLon", 0.0);
        start_lat = intent.getDoubleExtra("StartLat", 0.0);
        start_lon = intent.getDoubleExtra("StartLon", 0.0);

        // 미니맵 구현 //
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.tmap_layout2);
        final RelativeLayout layout_outline = (RelativeLayout) findViewById(R.id.tmap_layout3);
        tmap = new TMapView(this);
        tmap.setSKPMapApiKey("a6c0cc4b-1fe2-369f-9daa-3edcfec32698");

        tmap.setTrackingMode(true);
        tmap.setIconVisibility(true);

        layout.addView(tmap);

        TMapGpsManager tmapgps_mini = new TMapGpsManager(NavigationActivity_2_StartNavi.this);
        tmapgps_mini.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        tmapgps_mini.setMinTime(0);
        tmapgps_mini.setMinDistance(0);
        tmapgps_mini.OpenGps();

        tpoint = tmapgps_mini.getLocation();

        StartPoint = new TMapPoint(start_lat, start_lon);
        EndPoint = new TMapPoint(target_lat, target_lon);

        //Minimap의 Polyline
        mini_Line = new TMapData();
        mini_Line.findPathData(StartPoint, EndPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                //tMapPolyLine.setLineColor(Color.BLUE);
                tmap.addTMapPath(tMapPolyLine); // Line을 그려주는 부분
            }
        });
        // 미니맵 보이기 & 감추기
        final ImageButton minimap_button = (ImageButton) findViewById(R.id.minimap);
        minimap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key == false) {
                    layout.setVisibility(View.INVISIBLE);
                    layout_outline.setVisibility(View.INVISIBLE);
                    minimap_button.setImageResource(R.drawable.minisee);
                    key = true;
                } else {
                    layout.setVisibility(View.VISIBLE);
                    layout_outline.setVisibility(View.VISIBLE);
                    minimap_button.setImageResource(R.drawable.minihide);
                    key = false;
                }
            }
        });

        ImageButton naviStop = (ImageButton)findViewById(R.id.stop_navi);
        naviStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeMessages(0);
                finish_navi = true; ///초기화
                com.example.jun.atest.BookMarkActivity.Bookmark = null; ///초기화
                finish();
            }
        });

        // 미니맵 구현 //
    }
}
