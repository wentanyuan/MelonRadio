package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.AddressBook;
import com.ddicar.melonradio.service.AddContactManager;
import com.ddicar.melonradio.service.UserManager;


public class VerificationView extends AbstractView {


    private EditText message;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        TextView cancel = (TextView)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.ADD_FROM_PHONE_BOOK);
            }
        });

        TextView send = (TextView)view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddContactManager manager = AddContactManager.getInstance();

                manager.sendInvitation(message.getText().toString());

            }
        });

        message = (EditText) view.findViewById(R.id.text_message);

        UserManager manager = UserManager.getInstance();
        message.setText("我是" + manager.getUser().name);

        TextView clearText = (TextView)view.findViewById(R.id.clear_text);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.setText("");
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
