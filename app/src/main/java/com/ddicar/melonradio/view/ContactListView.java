package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.ContactAdapter;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.ContactManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ContactListView extends AbstractView {


    private ContactAdapter contactAdapter;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        ImageView add = (ImageView) view.findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.ADD_CONTACT_VIEW);
            }
        });

        RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
            }
        });


        RelativeLayout newFriends = (RelativeLayout) view.findViewById(R.id.new_friends);
        newFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.NEW_FRIENDS);
            }
        });

        RelativeLayout invitation = (RelativeLayout) view.findViewById(R.id.invitation);
        invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.INVITATION);
            }
        });

        RelativeLayout historyGroup = (RelativeLayout) view.findViewById(R.id.history_group);
        historyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.HISTORY_GROUP);
            }
        });


        ListView contactList = (ListView) view.findViewById(R.id.list_contacts);

        contactAdapter = new ContactAdapter();
        contactList.setAdapter(contactAdapter);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.instance.switchScreen(ViewFlyweight.CONTACT_VIEW);

                ViewFlyweight.CONTACT_VIEW.render(position);
            }
        });

        ContactManager manager = ContactManager.getInstance();
        manager.listContacts();
    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {

    }

    @Override
    public void onBackPressed() {
        MainActivity.instance.switchScreen(ViewFlyweight.ADD_CONTACT_VIEW);
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
        contactAdapter.render();
    }
}
