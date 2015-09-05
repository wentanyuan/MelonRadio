package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.R;


public class MapView extends AbstractView {


    private RelativeLayout markerLayout;
    private ImageView heightLimit;
    private ImageView police;
    private ImageView gas;
    private RelativeLayout oilPriceDialog;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {


        ImageView addPoint = (ImageView)view.findViewById(R.id.add_point);

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerLayout.setVisibility(View.VISIBLE);
            }
        });

        markerLayout = (RelativeLayout)view.findViewById(R.id.marker_layout);


        heightLimit = (ImageView)view.findViewById(R.id.height_limit_icon);

        heightLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerLayout.setVisibility(View.INVISIBLE);
            }
        });

        police = (ImageView)view.findViewById(R.id.police_icon);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerLayout.setVisibility(View.INVISIBLE);
            }
        });

        gas = (ImageView)view.findViewById(R.id.gas_icon);
        gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerLayout.setVisibility(View.INVISIBLE);
                oilPriceDialog.setVisibility(View.VISIBLE);
            }
        });

        oilPriceDialog = (RelativeLayout)view.findViewById(R.id.oil_price_dialog);

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
