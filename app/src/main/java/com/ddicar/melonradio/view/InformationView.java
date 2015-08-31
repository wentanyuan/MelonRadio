package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;


public class InformationView extends AbstractView {


    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {


        ImageView addDialog = (ImageView)view.findViewById(R.id.add_dialog);
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        ImageView addContact = (ImageView)view.findViewById(R.id.contact_list);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.CONTACT_LIST_VIEW);
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
