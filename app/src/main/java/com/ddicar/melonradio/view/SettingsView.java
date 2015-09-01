package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;


public class SettingsView extends AbstractView {
    private RelativeLayout back;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        back = (RelativeLayout) view.findViewById(R.id.cancel);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
                ViewFlyweight.MAIN_VIEW.gotoMyView();
            }
        });


        RelativeLayout changePassword = (RelativeLayout) view.findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.CHANGE_PASSWORD);
            }
        });



        RelativeLayout feedback = (RelativeLayout) view.findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.FEEDBACK);
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
