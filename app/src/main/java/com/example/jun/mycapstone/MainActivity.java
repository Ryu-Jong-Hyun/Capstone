package com.example.jun.mycapstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    Button Navibutton = (Button)findViewById(R.id.button_Navigation);
    Button MapViewbutton = (Button)findViewById(R.id.button_MapView);
    Button BookMarkbutton = (Button)findViewById(R.id.button_BookMark);
    Button Optionbutton = (Button)findViewById(R.id.button_Option);

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
            startActivityForResult(intent,GET_STRING);
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

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "뒤로 버튼을 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_STRING) {
            alertDialog.dismiss();
        }
    }

}
