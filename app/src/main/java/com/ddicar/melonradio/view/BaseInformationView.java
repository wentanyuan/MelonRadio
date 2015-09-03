package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.UserManager;

public class BaseInformationView extends AbstractView {
    private static final String TAG = "BaseInformationView";
    private RelativeLayout back;
    private RelativeLayout dialog;
    private RelativeLayout cancelPhoto;
    private RelativeLayout album;
    private RelativeLayout takePhoto;
    private RelativeLayout avatar;
    private TextView name;
    private TextView phone;
    private TextView team;
    private TextView plate;
    private TextView brand;
    private TextView model;
    private TextView parameters;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        adjustUI();

        back = (RelativeLayout) view.findViewById(R.id.cancel);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
                ViewFlyweight.MAIN_VIEW.gotoMyView();
            }
        });

        avatar = (RelativeLayout) view.findViewById(R.id.avatar);
        avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "avatar clicked");
                dialog.setVisibility(View.VISIBLE);
            }
        });

        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        team = (TextView) view.findViewById(R.id.team);
        plate = (TextView) view.findViewById(R.id.plate);
        brand = (TextView) view.findViewById(R.id.brand);
        model = (TextView) view.findViewById(R.id.model);
        parameters = (TextView) view.findViewById(R.id.parameters);


        UserManager userManager = UserManager.getInstance();
        name.setText(userManager.getUser().name);
        phone.setText(userManager.getUser().phone);
        team.setText(userManager.getUser().team.name);
        plate.setText(userManager.getUser().truck.plate);
        brand.setText(userManager.getUser().truck.brand);
        model.setText(userManager.getUser().truck.model);
        parameters.setText(userManager.getUser().truck.parameters());

        dialog = (RelativeLayout) view.findViewById(R.id.dialog);

        takePhoto = (RelativeLayout) view.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);
            }
        });

        album = (RelativeLayout) view.findViewById(R.id.album);
        album.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);
            }
        });

        cancelPhoto = (RelativeLayout) view.findViewById(R.id.cancel_photo);
        cancelPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void adjustUI() {

    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX,
                        float velocityY) {

    }

    @Override
    public void onBackPressed() {
        MainActivity.instance.switchScreen(ViewFlyweight.SOFTWARE_SETTING);

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
