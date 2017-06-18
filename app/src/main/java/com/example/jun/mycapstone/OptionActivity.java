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









        Switch Bibration_switch = (Switch)findViewById(R.id.Vibration_switch);
        Bibration_switch.setChecked(vibe_checked);
        Bibration_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    vibe_checked = true;
                    Toast.makeText(OptionActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    if(!change_vibration.equals("vibe_on")){


                    }
                }
                else {
                    vibe_checked = false;
                    Toast.makeText(OptionActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    if(change_vibration.equals("vibe_on")){


                    }
                }
            }
        });








        Switch Sound_switch = (Switch)findViewById(R.id.Sound_switch);
        Sound_switch.setChecked(sound_checked);
        Sound_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    sound_checked = true;
                    Toast.makeText(OptionActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    if(!change_sound.equals("sound_on")){


                    }
                }
                else {
                    sound_checked = false;
                    Toast.makeText(OptionActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    if(change_sound.equals("sound_on")){


                    }
                }
            }
        });






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


                                break;
                            case 1:
                                DrawSurfaceView.C_point = "Green";


                                break;
                            case 2:
                                DrawSurfaceView.C_point = "Blue";


                                break;
                        }
                    }
                });






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
