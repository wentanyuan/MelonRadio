package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.InvitationAdapter;
import com.ddicar.melonradio.service.InvitationManager;


public class InvitationView extends AbstractView {


    private InvitationAdapter invitationAdapter;

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

        ListView invitations = (ListView) view.findViewById(R.id.rooms);
        invitationAdapter = new InvitationAdapter();
        invitations.setAdapter(invitationAdapter);

        InvitationManager invitationManager = InvitationManager.getInstance();

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

    public void render() {
        invitationAdapter.render();
    }
}
