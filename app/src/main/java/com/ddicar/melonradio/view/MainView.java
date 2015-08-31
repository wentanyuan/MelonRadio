package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.R;

/**
 * Created by Stephen on 15/8/29.
 */
public class MainView extends AbstractView {

    private RelativeLayout content;
    private RelativeLayout info;
    private RelativeLayout waybill;
    private RelativeLayout map;
    private RelativeLayout my;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        content = (RelativeLayout) view.findViewById(R.id.content);
        content.removeAllViews();
        content.addView(ViewFlyweight.INFO_VIEW.view);

        info = (RelativeLayout) view.findViewById(R.id.information_layout);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content.removeAllViews();
                content.addView(ViewFlyweight.INFO_VIEW.view);
                ViewFlyweight.INFO_VIEW.auto();
            }
        });


        waybill = (RelativeLayout) view.findViewById(R.id.waybill_layout);

        waybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content.removeAllViews();
                content.addView(ViewFlyweight.WAY_BILL_VIEW.view);
                ViewFlyweight.WAY_BILL_VIEW.auto();
            }
        });


        map = (RelativeLayout) view.findViewById(R.id.map_layout);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content.removeAllViews();
                content.addView(ViewFlyweight.MAP_VIEW.view);
                ViewFlyweight.MAP_VIEW.auto();
            }
        });

        my = (RelativeLayout) view.findViewById(R.id.my_layout);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeAllViews();
                content.addView(ViewFlyweight.MY_VIEW.view);
                ViewFlyweight.MY_VIEW.auto();
            }
        });

    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
