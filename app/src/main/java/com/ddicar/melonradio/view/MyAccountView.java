package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;


public class MyAccountView extends AbstractView {

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
