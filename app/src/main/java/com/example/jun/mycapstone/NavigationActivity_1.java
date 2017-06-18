package com.example.jun.mycapstone;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class NavigationActivity_1 extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    private TMapView tmap;
    private TMapRouteCreater rCreater;
    TMapPoint StartPoint,EndPoint;
    TMapPOIItem item;
    TMapData tmapdata, tmapdata2, tmapdata3;
    TMapPoint tpoint;
    static double num1, num2;
    static double target1, target2;
    static String string1, string2;
    String address, a;
    String test;
    static int i,j =0 ;
    boolean SearchON;
    static ArrayList<String> turnType = new ArrayList<String>();   //turnType 값
    ArrayList<String> Point_cood = new ArrayList<String>();     // X, Y 좌표
    static ArrayList<String> Point_X = new ArrayList<String>();        // X좌표만
    static ArrayList<String> Point_Y = new ArrayList<String>();        // Y좌표만
    static ArrayList<String> DB_num = new ArrayList<String>();

    public static double lon, lat; //현재 위도와 경도를 저장하기 위한 변수
    public static double renewalLat; //위도 좌표 보내주는 변수
    public static double renewalLon; //경도 좌표 보내주는 변수

    String[] items_na;

    static double allDis;

    @Override
    public void onLocationChange(Location location) {
        tmap.setLocationPoint(location.getLongitude(), location.getLatitude());

        tmap.setZoomLevel(15);
        tmap.setCenterPoint(tpoint.getLongitude(), tpoint.getLatitude());

        lon = location.getLongitude();
        lat = location.getLatitude();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_navigation_1);

        //DB
        final DBManager dbManager = new DBManager(getApplicationContext(), "BookMarkInfo.db", null, 1);
        final StringTokenizer DBstok = new StringTokenizer(dbManager.select());  // DB에서 stok으로 한번에
        while(DBstok.hasMoreTokens()) {   // stok에서 ArrayList로 토큰별로 담음
            DB_num.add(DBstok.nextToken());
        }
        final Button search = (Button)findViewById(R.id.startSearch);

        final DBManager dbManager2 = new DBManager(getApplicationContext(), "OptionInfo.db", null, 1);

        if(dbManager2.Vibe_select().equals("on")){
            OptionActivity.vibe_checked = true;
        } else {
            OptionActivity.vibe_checked = false;
        }

        if(dbManager2.Sound_select().equals("on")){
            OptionActivity.sound_checked = true;
        } else {
            OptionActivity.sound_checked = false;
        }

        initTmap();

        /// BookMark Test Start ///
        if (com.example.jun.atest.BookMarkActivity.Bookmark != null)
        {
            Intent intent = getIntent();
            num1 = intent.getDoubleExtra("Lat", 0.0);
            num2 = intent.getDoubleExtra("Lon", 0.0);

            Toast.makeText(NavigationActivity_1.this, com.example.jun.atest.BookMarkActivity.Bookmark, Toast.LENGTH_SHORT).show();
            rCreater = new TMapRouteCreater();
            string2 = com.example.jun.atest.BookMarkActivity.Bookmark;
            SearchPoiInfo endPoint = rCreater.requestInfo(string2);
            target1 = endPoint.getPois().get("poi").get(0).getNoorLat();
            target2 = endPoint.getPois().get("poi").get(0).getNoorLon();

            StartPoint = new TMapPoint(num1, num2);
            EndPoint = new TMapPoint(endPoint.getPois().get("poi").get(0).getNoorLat(), endPoint.getPois().get("poi").get(0).getNoorLon());

            tmapdata3 = new TMapData();

            // 여기서 턴타입 한번 더 뽑자
            ////////////////////// TurnType 추출////////////////////////////////
            try {
                Document doc = tmapdata3.findPathDataAll(StartPoint, EndPoint);
                NodeList nodelist_placemark = doc.getElementsByTagName("Placemark");// xml 형태의 doc를 해당태그가 placemark로 시작하는 데이터들을 넣어줌
                //   Toast.makeText(NavigationActivity_1.this, "값은 : " + nodelist_placemark.getLength(), Toast.LENGTH_SHORT).show();
                for (i = 0; i < nodelist_placemark.getLength(); i++) {
                    Element element_placemark = (Element) nodelist_placemark.item(i);// placemark 태그값을 가지는 것중 첫번째 값

                    // Turn Type값 뽑는곳 //
                    NodeList nodelist_placemark_turnType = element_placemark.getElementsByTagName("tmap:turnType");// 그 첫번째 값에서 turnType을 뽑아냄
                    Node node_place_turnType = nodelist_placemark_turnType.item(0); // turnType안에 있는 첫번째 값
                    if (node_place_turnType == null)
                        continue;

                    ////좌표 뽑는곳/////
                    NodeList nodelist_placemark_point = element_placemark.getElementsByTagName("Point");
                    Element point_cood = (Element) nodelist_placemark_point.item(0);
                    NodeList nodelist_placemark_point_cood = point_cood.getElementsByTagName("coordinates");
                    Node node_place_point_cood = nodelist_placemark_point_cood.item(0);

                    // 좌표, Turn Type값 대입//
                    if (node_place_turnType.getFirstChild() != null) {
                        turnType.add(node_place_turnType.getFirstChild().getNodeValue());
                        Point_cood.add(node_place_point_cood.getFirstChild().getNodeValue());
                        StringTokenizer Str = new StringTokenizer(Point_cood.get(j), ",");
                        Point_X.add(Str.nextToken());
                        Point_Y.add(Str.nextToken());
                        j++;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(NavigationActivity_1.this, "오류 발생!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            ////////////////////////////TurnType 추출//////////////////////////////////////
            Toast.makeText(NavigationActivity_1.this, "즐겨찾기 길안내를 시작합니다.", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(NavigationActivity_1.this, NavigationActivity_2_StartNavi.class);
            finish();
            intent2.putExtra("TargetLat", target1);
            intent2.putExtra("TargetLon", target2);
            intent2.putExtra("StartLat", num1);
            intent2.putExtra("StartLon", num2);
            startActivity(intent2);
        }
        /// BookMark Test End ///


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lat == 0 || lon == 0) {
                    Toast.makeText(NavigationActivity_1.this,"Please wait..",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NavigationActivity_1.this,NavigationActivity_1.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    tmapdata.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String s) {
                            address = s;
                            tmapdata.findAllPOI(address, new TMapData.FindAllPOIListenerCallback() {
                                @Override
                                public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        item = arrayList.get(i);
                                        a = item.name;
                                    }
                                }
                            });
                        }
                    });

                    if (a == null) {
                        search.performClick();
                    } else {
                        EditText Start_text = (EditText) findViewById(R.id.Start_text);
                        Start_text.setText(a);
                        SearchON = true;
                    }
                }
                EditText Start_text = (EditText)findViewById(R.id.Start_text);
                Start_text.setText(a);
                SearchON = true;
            }
        });

        ////////////////////////////
        ////즐겨찾는 목적지 코딩////
        ////////////////////////////

        Button Target_button = (Button)findViewById(R.id.Target_Button);
        Target_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items_na = null;

                final DBManager bookMarkDB_navi = new DBManager(getApplicationContext(), "BookMarkInfo.db", null, 1);
                final ArrayList<String> DB_num_navi = new ArrayList<String>();
                final StringTokenizer stok_navi = new StringTokenizer(bookMarkDB_navi.select());  // DB에서 stok으로 한번에

                while (stok_navi.hasMoreTokens()) {   // stok에서 ArrayList로 토큰별로 담음
                    DB_num_navi.add(stok_navi.nextToken());
                }
                items_na = DB_num_navi.toArray(new String[DB_num_navi.size()]);

                final AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity_1.this);
                builder.setTitle("자주가는 목적지");
                builder.setItems(items_na, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(NavigationActivity_1.this,items_na[i] + "를 선택합니다.", Toast.LENGTH_SHORT).show();
                        EditText This = (EditText)findViewById(R.id.End_text);
                        This.setText(items_na[i]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void initTmap() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.tmap_layout);
        tmap = new TMapView(this);
        tmap.setSKPMapApiKey("a6c0cc4b-1fe2-369f-9daa-3edcfec32698");

        tmapdata = new TMapData();
        item = new TMapPOIItem();

        TMapGpsManager tmapgps = new TMapGpsManager(NavigationActivity_1.this);
        tmapgps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        tmapgps.setMinTime(0);
        tmapgps.setMinDistance(0);
        tmapgps.OpenGps();

        tpoint = tmapgps.getLocation();

        ImageButton FindRoute = (ImageButton)findViewById(R.id.button_FindRoute);
        FindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_start = (EditText) findViewById(R.id.Start_text);
                EditText edit_end = (EditText) findViewById(R.id.End_text);

                if (edit_start.getText().toString().isEmpty() || edit_end.getText().toString().isEmpty()) {
                    Toast.makeText(NavigationActivity_1.this, "출발지 혹은 목적지를 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    string1 = edit_start.getText().toString();
                    string2 = edit_end.getText().toString();

                    rCreater = new TMapRouteCreater();
                    SearchPoiInfo startPoint = rCreater.requestInfo(string1);
                    SearchPoiInfo endPoint = rCreater.requestInfo(string2);

                    // 두번째 실행시 에러 부분
                    target1 = endPoint.getPois().get("poi").get(0).getNoorLat();
                    target2 = endPoint.getPois().get("poi").get(0).getNoorLon();


                    if (SearchON == true) {
                        StartPoint = new TMapPoint(lat, lon);
                    } else {
                        StartPoint = new TMapPoint(startPoint.getPois().get("poi").get(0).getNoorLat(), startPoint.getPois().get("poi").get(0).getNoorLon());
                    }
                    EndPoint = new TMapPoint(endPoint.getPois().get("poi").get(0).getNoorLat(), endPoint.getPois().get("poi").get(0).getNoorLon());

                    Toast.makeText(NavigationActivity_1.this, "길안내 루트를 생성합니다.", Toast.LENGTH_LONG).show();
                    SearchON = false;


                    //Tmap 실행
                    tmapdata = new TMapData();
                    tmapdata.findPathData(StartPoint, EndPoint, new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
                            //tMapPolyLine.setLineColor(Color.BLUE);
                            tmap.addTMapPath(tMapPolyLine); // Line을 그려주는 부분
                            allDis = tMapPolyLine.getDistance();
                        }
                    });

                    ////////////////////// TurnType 추출////////////////////////////////
                    tmapdata2 = new TMapData();
                    try {
                        Document doc = tmapdata2.findPathDataAll(StartPoint, EndPoint);
                        NodeList nodelist_placemark = doc.getElementsByTagName("Placemark");// xml 형태의 doc를 해당태그가 placemark로 시작하는 데이터들을 넣어줌
                        for (i = 0; i < nodelist_placemark.getLength(); i++) {
                            Element element_placemark = (Element) nodelist_placemark.item(i);// placemark 태그값을 가지는 것중 첫번째 값

                            // Turn Type값 뽑는곳 //
                            NodeList nodelist_placemark_turnType = element_placemark.getElementsByTagName("tmap:turnType");// 그 첫번째 값에서 turnType을 뽑아냄
                            Node node_place_turnType = nodelist_placemark_turnType.item(0); // turnType안에 있는 첫번째 값
                            if (node_place_turnType == null)
                                continue;

                            ////좌표 뽑는곳/////
                            NodeList nodelist_placemark_point = element_placemark.getElementsByTagName("Point");
                            Element point_cood = (Element) nodelist_placemark_point.item(0);
                            NodeList nodelist_placemark_point_cood = point_cood.getElementsByTagName("coordinates");
                            Node node_place_point_cood = nodelist_placemark_point_cood.item(0);

                            // 좌표, Turn Type값 대입//
                            if (node_place_turnType.getFirstChild() != null) {
                                turnType.add(node_place_turnType.getFirstChild().getNodeValue());
                                Point_cood.add(node_place_point_cood.getFirstChild().getNodeValue());
                                StringTokenizer Str = new StringTokenizer(Point_cood.get(j), ",");
                                // 두번째 실행시 에러 부분
                                Point_X.add(Str.nextToken());
                                Point_Y.add(Str.nextToken());
                                j++;
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(NavigationActivity_1.this, "오류 발생!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    ////////////////////////////TurnType 추출//////////////////////////////////////
                }
            }
        });

        // 지도화면 설정후 길안내 시작
        ImageButton StartNavi = (ImageButton)findViewById(R.id.button_StartNavi);
        StartNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_start = (EditText) findViewById(R.id.Start_text);
                EditText edit_end = (EditText) findViewById(R.id.End_text);

                if (edit_start.getText().toString().isEmpty() || edit_end.getText().toString().isEmpty()) {
                    Toast.makeText(NavigationActivity_1.this, "출발지 혹은 목적지를 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(NavigationActivity_1.this, NavigationActivity_2_StartNavi.class);
                    intent.putExtra("TargetLat", target1);
                    intent.putExtra("TargetLon", target2);
                    intent.putExtra("StartLat", lat);
                    intent.putExtra("StartLon", lon);
                    finish();
                    startActivity(intent);
                }
            }
        });

        tmap.setTrackingMode(true);
        tmap.setIconVisibility(true);
        layout.addView(tmap);
    }

    public static double TargetLat() {
        double TarLat = target1;
        return TarLat;
    }

    public static double TargetLon() {
        double TarLon = target2;
        return TarLon;
    }

    public static String MyTarget() {
        String MyTarget = string2;
        return MyTarget;
    }

    public static ArrayList<String> PointX(){
        ArrayList<String> Px = Point_X;
        return Px;
    }
    public static ArrayList<String> PointY(){
        ArrayList<String> Py = Point_Y;
        return Py;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}