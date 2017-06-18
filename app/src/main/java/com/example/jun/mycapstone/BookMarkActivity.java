package com.example.jun.mycapstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jun.mycapstone.DBManager;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class BookMarkActivity extends AppCompatActivity  {

    static String Bookmark;
    static double BLat, BLon;
    static String str1;

    static String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark2);

        final DBManager dbManager = new DBManager(getApplicationContext(), "BookMarkInfo.db", null, 1);

        final ArrayList<String> DB_num = new ArrayList<String>();
        final StringTokenizer stok = new StringTokenizer(dbManager.select());  // DB에서 stok으로 한번에
        while(stok.hasMoreTokens()) {   // stok에서 ArrayList로 토큰별로 담음
            DB_num.add(stok.nextToken());
        }
        items = DB_num.toArray(new String[DB_num.size()]);     // ArrayList에서 items로
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);       // items를 리스트로
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);


        // 즐겨찾기 길안내 //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                final String item = String.valueOf(parent.getItemAtPosition(i));
                AlertDialog.Builder builder = new AlertDialog.Builder(BookMarkActivity.this);
                builder.setMessage("즐겨찾기로 길안내를 시작할까요?");
                builder.setTitle("즐겨찾기 길안내")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //여기에 구현
                                Intent intent = new Intent(BookMarkActivity.this, NavigationActivity_1.class);
                                //   intent.putExtra("BookMark", item.toString());
                                if(MainActivity.Lat  == 0){
                                    Toast.makeText(BookMarkActivity.this, "현재 위치를 받아오는 중입니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    intent.putExtra("Lat", MainActivity.Lat);
                                    intent.putExtra("Lon", MainActivity.Lon);
                                    Bookmark = item;
                                    BLat = MainActivity.Lat;
                                    BLon = MainActivity.Lon;
                                    startActivity(intent);
                                }
                                //  Bookmark = null;
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("즐겨찾기 길안내");
                alert.show();
                // Toast.makeText(BookMarkActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });
        // 즐겨찾기 길안내 //

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = String.valueOf(parent.getItemAtPosition(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(BookMarkActivity.this);
                builder.setMessage("이 길을 삭제할까요?");
                builder.setTitle("즐겨찾기 길삭제")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.delete(item);
                                Toast.makeText(BookMarkActivity.this, item + "을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                Intent t = new Intent(BookMarkActivity.this, BookMarkActivity.class);
                                startActivity(t);
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("즐겨찾기 길삭제");
                alert.show();

                return true;
            }
        });

        // 즐겨찾기 ADD 버튼 //
        ImageButton add_Button = (ImageButton)findViewById(R.id.Add_button);
        add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookMarkActivity.this);
                builder.setMessage("즐겨찾기를 추가할까요?");
                builder.setTitle("즐겨찾기 추가")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText Edit_BookMark = (EditText)findViewById(R.id.Edit_BookMark);
                                str1 = Edit_BookMark.getText().toString();
                                dbManager.insert(Edit_BookMark.getText().toString());
                                int num = stok.countTokens();
                                DB_num.add(Edit_BookMark.getText().toString());
                                items = DB_num.toArray(new String[DB_num.size()]);

                                Toast.makeText(BookMarkActivity.this, str1 + "을 추가했다.", Toast.LENGTH_SHORT).show();
                                Intent t = new Intent(BookMarkActivity.this, BookMarkActivity.class);
                                startActivity(t);
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                items = DB_num.toArray(new String[DB_num.size()]);
                AlertDialog alert = builder.create();
                alert.setTitle("즐겨찾기 추가");
                alert.show();
            }
        });
        // 즐겨찾기 ADD 버튼 //

    }
}


