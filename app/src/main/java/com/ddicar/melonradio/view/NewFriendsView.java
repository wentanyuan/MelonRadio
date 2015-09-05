package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.FriendMessageAdapter;
import com.ddicar.melonradio.service.FriendManager;


public class NewFriendsView extends AbstractView {


    private FriendMessageAdapter friendAdapter;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.ADD_CONTACT_VIEW);
            }
        });

        ListView friends = (ListView) view.findViewById(R.id.friends);
        friendAdapter = new FriendMessageAdapter();
        friends.setAdapter(friendAdapter);

        FriendManager manager = FriendManager.getInstance();
        manager.listFriendMessage();
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


    public void render() {
        friendAdapter.render();
    }
}
