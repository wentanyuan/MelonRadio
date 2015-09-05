package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.InviteAdapter;
import com.ddicar.melonradio.service.InvitationManager;
import com.ddicar.melonradio.service.InviteManager;


public class InviteView extends AbstractView {


    private InviteAdapter inviteContactAdapter;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        TextView cancel = (TextView)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.CHAT_ROOM);
            }
        });

        TextView invite = (TextView)view.findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.CHAT_ROOM);
            }
        });

        ListView contactList = (ListView) view.findViewById(R.id.list_contacts);

        inviteContactAdapter = new InviteAdapter();
        contactList.setAdapter(inviteContactAdapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.instance.switchScreen(ViewFlyweight.CHAT_ROOM);
            }
        });

        InviteManager inviteManager = InviteManager.getInstance();
        inviteManager.listInvites();
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
        inviteContactAdapter.render();
    }
}
