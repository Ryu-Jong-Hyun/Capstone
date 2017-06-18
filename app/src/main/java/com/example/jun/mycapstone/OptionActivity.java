package com.example.jun.mycapstone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jun.atest.R;

public class OptionActivity extends AppCompatActivity {
    static boolean vibe_checked;
    static boolean sound_checked;
    static String change_sound;
    static String change_vibration;

    AlertDialog alertDialog;

    String [] items = {"Orange Pointer", "Green Pointer", "Blue Pointer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        final DBManager dbManager = new DBManager(getApplicationContext(), "OptionInfo.db", null, 1);

        change_vibration = dbManager.Vibe_select();

        if(change_vibration.equals("vibe_on")){
            vibe_checked = true;
        } else {
            vibe_checked = false;
        }

        Switch Bibration_switch = (Switch)findViewById(R.id.Vibration_switch);
        Bibration_switch.setChecked(vibe_checked);
        Bibration_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vibe_checked = true;
                    Toast.makeText(OptionActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    if(!change_vibration.equals("vibe_on")){
                        DBManager.update_sep = 1;
                        dbManager.update();
                    }
                }
                else {
                    vibe_checked = false;
                    Toast.makeText(OptionActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    if(change_vibration.equals("vibe_on")){
                        DBManager.update_sep = 2;
                        dbManager.update();
                    }
                }
            }
        });

        change_sound = dbManager.Sound_select();

        if(change_sound.equals("sound_on")){
            sound_checked = true;
        } else {
            sound_checked = false;
        }

        Switch Sound_switch = (Switch)findViewById(R.id.Sound_switch);
        Sound_switch.setChecked(sound_checked);
        Sound_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sound_checked = true;
                    Toast.makeText(OptionActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    if(!change_sound.equals("sound_on")){
                        DBManager.update_sep = 3;
                        dbManager.update();
                    }
                }
                else {
                    sound_checked = false;
                    Toast.makeText(OptionActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    if(change_sound.equals("sound_on")){
                        DBManager.update_sep = 4;
                        dbManager.update();
                    }
                }
            }
        });

        final Button Point_button = (Button)findViewById(R.id.PointOptionButton);
        if(dbManager.Orange_select().equals("orange_on")) {
            Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo,0);
        } else if(dbManager.Green_select().equals("green_on")){
            Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo_g,0);
        } else if(dbManager.Blue_select().equals("blue_on")){
            Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo_b,0);
        } else {
            Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo,0);
        }

        Point_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OptionActivity.this, alertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alertDialog = builder.create();
                builder.setTitle("화살표 색상 변경");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(OptionActivity.this, items[i] + "를 선택합니다.", Toast.LENGTH_SHORT).show();
                        switch (i) {
                            case 0:
                                DrawSurfaceView.C_point = "Orange";
                                DBManager.update_sep = 5;
                                dbManager.update();
                                Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo,0);
                                break;
                            case 1:
                                DrawSurfaceView.C_point = "Green";
                                DBManager.update_sep = 6;
                                dbManager.update();
                                Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo_g,0);
                                break;
                            case 2:
                                DrawSurfaceView.C_point = "Blue";
                                DBManager.update_sep = 7;
                                dbManager.update();
                                Point_button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.minigo_b,0);
                                break;
                        }
                    }
                });

                if(dbManager.Orange_select().equals("orange_on")) {
                    builder.setIcon(R.drawable.go);
                } else if(dbManager.Green_select().equals("green_on")){
                    builder.setIcon(R.drawable.go_g);
                } else if(dbManager.Blue_select().equals("blue_on")){
                    builder.setIcon(R.drawable.go_b);
                } else {
                    builder.setIcon(R.drawable.go);
                }

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
