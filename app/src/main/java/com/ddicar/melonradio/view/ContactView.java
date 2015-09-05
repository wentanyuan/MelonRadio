package com.ddicar.melonradio.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.ContactManager;


public class ContactView extends AbstractView {


    private ImageView avatar;
    private TextView name;
    private TextView team;
    private TextView plate;
    private TextView brand;
    private TextView model;
    private TextView length;
    private TextView weight;
    private TextView volume;
    private RelativeLayout phoneCall;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.CONTACT_LIST_VIEW);
            }
        });


        avatar = (ImageView) view.findViewById(R.id.avatar);

        name = (TextView) view.findViewById(R.id.name);

        team = (TextView) view.findViewById(R.id.team);
        plate = (TextView) view.findViewById(R.id.plate);
        brand = (TextView) view.findViewById(R.id.brand);
        model = (TextView) view.findViewById(R.id.model);
        length = (TextView) view.findViewById(R.id.length);
        weight = (TextView) view.findViewById(R.id.weight);
        volume = (TextView) view.findViewById(R.id.volume);

        render();


        phoneCall = (RelativeLayout) view.findViewById(R.id.phone_call);
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactManager manager = ContactManager.getInstance();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + manager.getCurrentContact().phone));
                MainActivity.instance.startActivity(intent);
            }
        });

    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {

    }

    @Override
    public void onBackPressed() {
        MainActivity.instance.switchScreen(ViewFlyweight.CONTACT_LIST_VIEW);
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

    public void render(int position) {
        ContactManager manager = ContactManager.getInstance();
        manager.setCurrentPosition(position);
        render();
    }

    private void render() {

        //TODO show avatar
//        avatar;

        ContactManager manager = ContactManager.getInstance();
        User contact = manager.getCurrentContact();

        name.setText(contact.name);
        team.setText(contact.team.name);
        plate.setText(contact.truck.plate);
        brand.setText(contact.truck.brand);
        model.setText(contact.truck.model);
        length.setText(String.valueOf(contact.truck.length));
        weight.setText(String.valueOf(contact.truck.weight));
        volume.setText(String.valueOf(contact.truck.volume));
    }
}
