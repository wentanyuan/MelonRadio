package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView infoImage;
    private ImageView waybillImage;
    private ImageView mapImage;
    private ImageView myImage;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        content = (RelativeLayout) view.findViewById(R.id.content);
        info = (RelativeLayout) view.findViewById(R.id.information_layout);

        infoImage = (ImageView) view.findViewById(R.id.information_image);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoInfoView();
            }
        });


        waybill = (RelativeLayout) view.findViewById(R.id.waybill_layout);
        waybillImage = (ImageView) view.findViewById(R.id.waybill_image);

        waybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoWayBillView();
            }
        });


        map = (RelativeLayout) view.findViewById(R.id.map_layout);
        mapImage = (ImageView) view.findViewById(R.id.map_image);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoMapView();
            }
        });

        my = (RelativeLayout) view.findViewById(R.id.my_layout);
        myImage = (ImageView) view.findViewById(R.id.my_image);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMyView();
            }
        });

        gotoInfoView();

    }

    public void gotoMyView() {
        content.removeAllViews();
        content.addView(ViewFlyweight.MY_VIEW.view);
        ViewFlyweight.MY_VIEW.auto();
        mapImage.setImageResource(R.drawable.map_icon_normal);
        waybillImage.setImageResource(R.drawable.waybill_icon_normal);
        myImage.setImageResource(R.drawable.my_icon_press);
        infoImage.setImageResource(R.drawable.chat_icon_normal);
    }

    public void gotoMapView() {
        content.removeAllViews();
        content.addView(ViewFlyweight.MAP_VIEW.view);
        ViewFlyweight.MAP_VIEW.auto();
        mapImage.setImageResource(R.drawable.map_icon_press);
        waybillImage.setImageResource(R.drawable.waybill_icon_normal);
        myImage.setImageResource(R.drawable.my_icon_normal);
        infoImage.setImageResource(R.drawable.chat_icon_normal);
    }

    public void gotoWayBillView() {
        content.removeAllViews();
        content.addView(ViewFlyweight.WAY_BILL_VIEW.view);
        ViewFlyweight.WAY_BILL_VIEW.auto();

        mapImage.setImageResource(R.drawable.map_icon_normal);
        waybillImage.setImageResource(R.drawable.wallbell_icon_press);
        myImage.setImageResource(R.drawable.my_icon_normal);
        infoImage.setImageResource(R.drawable.chat_icon_normal);
    }

    public void gotoInfoView() {
        content.removeAllViews();
        content.addView(ViewFlyweight.INFO_VIEW.view);
        ViewFlyweight.INFO_VIEW.auto();
        mapImage.setImageResource(R.drawable.map_icon_normal);
        waybillImage.setImageResource(R.drawable.waybill_icon_normal);
        myImage.setImageResource(R.drawable.my_icon_normal);
        infoImage.setImageResource(R.drawable.chat_icon_press);
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
