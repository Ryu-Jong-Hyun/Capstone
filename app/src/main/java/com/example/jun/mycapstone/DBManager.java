package com.example.jun.mycapstone;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBManager extends SQLiteOpenHelper {
    static String DB_NAME = "BuildingInfo.db";
    static String PACKEGE = "com.example.jun.atest";
    static String DB = "BuildingInfo.db";

    static SQLiteDatabase.CursorFactory FACTORY = null;

    static int VERSION  = 1;
    static int update_sep= 0;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    public DBManager(Context context) {
        super(context, DB_NAME, FACTORY, VERSION);

        Toast.makeText(context , "DB 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();

        try {
            boolean bResult = isCheckDB(context);
            Log.i("MiniApp", "DB Check="+bResult);
            if(!bResult){
                copyDB(context);
            }else{

            }
        } catch (Exception e) {

        }
    }

    public boolean isCheckDB(Context mContext){
        String filePath = "/data/data/" + PACKEGE + "/databases/" + DB;
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;
    }

    public void copyDB(Context mContext){
        Log.d("MiniApp", "copyDB");
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/" + PACKEGE + "/databases";
        String filePath = "/data/data/" + PACKEGE + "/databases/" +DB;
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            InputStream is = manager.open("db/" + DB);
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            }else{
                folder.mkdirs();
            }

            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];

            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookMarkInfo (id INTEGER PRIMARY KEY AUTOINCREMENT, destination TEXT);");
        db.execSQL("CREATE TABLE OptionInfo (id INTEGER PRIMARY KEY AUTOINCREMENT, setting TEXT);");
        db.execSQL("INSERT INTO OptionInfo VALUES(null,'vibe_on'), (null,'sound_on'), (null,'orange_on'),(null,'green_off'),(null,'blue_off');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String destinaion) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO BookMarkInfo VALUES(null,'"+destinaion+"');");
        db.close();
    }

    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM BookMarkInfo WHERE destination='" + item+ "';");
        db.close();
    }

    public void update(){
        SQLiteDatabase db = getWritableDatabase();
        switch (update_sep) {
            case 1:
                db.execSQL("UPDATE OptionInfo set setting = 'vibe_on' WHERE id = 1"); break;
            case 2:
                db.execSQL("UPDATE OptionInfo set setting = 'vibe_off' WHERE id = 1"); break;
            case 3:
                db.execSQL("UPDATE OptionInfo set setting = 'sound_on' WHERE id = 2"); break;
            case 4:
                db.execSQL("UPDATE OptionInfo set setting = 'sound_off' WHERE id = 2"); break;
            case 5:
                db.execSQL("UPDATE OptionInfo set setting = 'orange_on' WHERE id = 3");
                db.execSQL("UPDATE OptionInfo set setting = 'green_off' WHERE id = 4");
                db.execSQL("UPDATE OptionInfo set setting = 'blue_off' WHERE id = 5");
                break;
            case 6:
                db.execSQL("UPDATE OptionInfo set setting = 'orange_off' WHERE id = 3");
                db.execSQL("UPDATE OptionInfo set setting = 'green_on' WHERE id = 4");
                db.execSQL("UPDATE OptionInfo set setting = 'blue_off' WHERE id = 5");
                break;
            case 7:
                db.execSQL("UPDATE OptionInfo set setting = 'orange_off' WHERE id = 3");
                db.execSQL("UPDATE OptionInfo set setting = 'green_off' WHERE id = 4");
                db.execSQL("UPDATE OptionInfo set setting = 'blue_on' WHERE id = 5");
                break;
        }
        db.close();
    }

    public String select() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor c = db.rawQuery("SELECT * FROM BookMarkInfo;", null);
        while (c.moveToNext()) {
            result += c.getString(1)+" ";
        }
        return result;
    }

    public String Vibe_select() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor c = db.rawQuery("SELECT * FROM OptionInfo WHERE id = 1;", null);
        while (c.moveToNext()) {
            result = c.getString(1);
        }
        return result;
    }

    public String Sound_select() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor c = db.rawQuery("SELECT * FROM OptionInfo WHERE id = 2;", null);
        while (c.moveToNext()) {
            result = c.getString(1);
        }
        return result;
    }

    public String Orange_select() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor c = db.rawQuery("SELECT * FROM OptionInfo WHERE id = 3;", null);
        while (c.moveToNext()) {
            result = c.getString(1);
        }
        return result;
    }

    public String Green_select() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor c = db.rawQuery("SELECT * FROM OptionInfo WHERE id = 4;", null);
        while (c.moveToNext()) {
            result = c.getString(1);
        }
        return result;
    }

    public String Blue_select() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor c = db.rawQuery("SELECT * FROM OptionInfo WHERE id = 5;", null);
        while (c.moveToNext()) {
            result = c.getString(1);
        }
        return result;
    }
}
