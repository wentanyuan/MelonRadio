package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.AndroidUtil;

/**
 * Created by Stephen on 15/8/29.
 */
public class MainView extends AbstractView {

    private static final String TAG = "MainView";
    private RelativeLayout content;
    private RelativeLayout info;
    private RelativeLayout waybill;
    private RelativeLayout map;
    private RelativeLayout my;
    private ImageView infoImage;
    private ImageView waybillImage;
    private ImageView mapImage;
    private ImageView myImage;
    private TextView infoText;
    private TextView waybillText;
    private TextView mapText;
    private TextView myText;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        content = (RelativeLayout) view.findViewById(R.id.content);

        info = (RelativeLayout) view.findViewById(R.id.information_layout);
        infoImage = (ImageView) view.findViewById(R.id.information_image);
        infoText = (TextView) view.findViewById(R.id.information_text);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoInfoView();
            }
        });

        waybill = (RelativeLayout) view.findViewById(R.id.waybill_layout);
        waybillText = (TextView) view.findViewById(R.id.waybill_text);
        waybillImage = (ImageView) view.findViewById(R.id.waybill_image);
        waybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWayBillView();
            }
        });

        map = (RelativeLayout) view.findViewById(R.id.map_layout);
        mapImage = (ImageView) view.findViewById(R.id.map_image);
        mapText = (TextView) view.findViewById(R.id.map_text);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMapView();
            }
        });

        my = (RelativeLayout) view.findViewById(R.id.my_layout);
        myImage = (ImageView) view.findViewById(R.id.my_image);
        myText = (TextView) view.findViewById(R.id.my_text);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMyView();
            }
        });

        gotoInfoView();

    }

    public void gotoMyView() {
        Log.e(TAG, "MY_VIEW");
        content.removeAllViews();
        content.addView(ViewFlyweight.MY_VIEW.view);
        ViewFlyweight.MY_VIEW.auto();

        mapImage.setImageResource(R.drawable.map_icon_normal);
        waybillImage.setImageResource(R.drawable.waybill_icon_normal);
        myImage.setImageResource(R.drawable.my_icon_press);
        infoImage.setImageResource(R.drawable.chat_icon_normal);

        mapText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        waybillText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        myText.setTextColor(AndroidUtil.parseColor(R.color.main_color));
        infoText.setTextColor(AndroidUtil.parseColor(R.color.gray));
    }

    public void gotoMapView() {
        Log.e(TAG, "MAP_VIEW");
        content.removeAllViews();
        content.addView(ViewFlyweight.MAP_VIEW.view);
        ViewFlyweight.MAP_VIEW.auto();

        mapImage.setImageResource(R.drawable.map_icon_press);
        waybillImage.setImageResource(R.drawable.waybill_icon_normal);
        myImage.setImageResource(R.drawable.my_icon_normal);
        infoImage.setImageResource(R.drawable.chat_icon_normal);

        mapText.setTextColor(AndroidUtil.parseColor(R.color.main_color));
        waybillText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        myText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        infoText.setTextColor(AndroidUtil.parseColor(R.color.gray));
    }

    public void gotoWayBillView() {
        Log.e(TAG, "WAY_BILL_VIEW");
        content.removeAllViews();
        content.addView(ViewFlyweight.WAY_BILL_VIEW.view);
        ViewFlyweight.WAY_BILL_VIEW.auto();

        mapImage.setImageResource(R.drawable.map_icon_normal);
        waybillImage.setImageResource(R.drawable.wallbell_icon_press);
        myImage.setImageResource(R.drawable.my_icon_normal);
        infoImage.setImageResource(R.drawable.chat_icon_normal);

        mapText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        waybillText.setTextColor(AndroidUtil.parseColor(R.color.main_color));
        myText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        infoText.setTextColor(AndroidUtil.parseColor(R.color.gray));
    }

    public void gotoInfoView() {
        Log.e(TAG, "INFO_VIEW");
        content.removeAllViews();
        content.addView(ViewFlyweight.INFO_VIEW.view);
        ViewFlyweight.INFO_VIEW.auto();

        mapImage.setImageResource(R.drawable.map_icon_normal);
        waybillImage.setImageResource(R.drawable.waybill_icon_normal);
        myImage.setImageResource(R.drawable.my_icon_normal);
        infoImage.setImageResource(R.drawable.chat_icon_press);

        mapText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        waybillText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        myText.setTextColor(AndroidUtil.parseColor(R.color.gray));
        infoText.setTextColor(AndroidUtil.parseColor(R.color.main_color));
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
}
