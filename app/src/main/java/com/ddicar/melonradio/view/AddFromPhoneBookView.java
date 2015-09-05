package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.PhoneBookAdapter;
import com.ddicar.melonradio.service.AddressBookManager;


public class AddFromPhoneBookView extends AbstractView {


    private static final String TAG = "AddFromPhoneBookView";
    private PhoneBookAdapter phoneBookAdapter;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        Log.e(TAG, "auto");
        RelativeLayout cancel = (RelativeLayout)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.ADD_CONTACT_VIEW);
            }
        });

        ListView phoneBookList = (ListView) view.findViewById(R.id.phone_book_list);
        phoneBookAdapter = new PhoneBookAdapter();
        phoneBookList.setAdapter(phoneBookAdapter);

        AddressBookManager manager = AddressBookManager.getInstance();
        manager.listAddressBook();

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
        Log.e(TAG, "render");
        phoneBookAdapter.render();
    }
}
