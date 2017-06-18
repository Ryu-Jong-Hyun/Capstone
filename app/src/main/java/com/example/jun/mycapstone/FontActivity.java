package com.example.jun.atest;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jun on 2017-05-29.
 */

public class FontActivity extends FragmentActivity {

    public Typeface mtypeface = null;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(mtypeface == null) {

            mtypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Daum_Regular.ttf");

        }
        setGlobalFont(getWindow().getDecorView());
    }

    private void setGlobalFont(View view) {
        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int vgCnt = viewGroup.getChildCount();
                for(int i = 0; i<vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(mtypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }
}
