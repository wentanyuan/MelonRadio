package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;


public class InformationView extends AbstractView {


    private static final String TAG = "InformationView";
    private RelativeLayout dialog;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {


        dialog = (RelativeLayout) view.findViewById(R.id.dialog);

        LinearLayout createChatRoom = (LinearLayout) view.findViewById(R.id.create_chat_room);
        createChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);
                MainActivity.instance.switchScreen(ViewFlyweight.CREATE_CHAT_ROOM);
            }
        });


        LinearLayout addFriend = (LinearLayout) view.findViewById(R.id.add_friend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);
                MainActivity.instance.switchScreen(ViewFlyweight.ADD_CONTACT_VIEW);
            }
        });

        ImageView addDialog = (ImageView) view.findViewById(R.id.add_dialog);
        addDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "add_dialog clicked");
                dialog.setVisibility(View.VISIBLE);
            }
        });

        ImageView contactList = (ImageView) view.findViewById(R.id.contact_list);
        contactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "contact_list clicked");
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
