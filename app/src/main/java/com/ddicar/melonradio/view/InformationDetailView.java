package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.GroupMessageAdapter;
import com.ddicar.melonradio.model.MessageGroup;
import com.ddicar.melonradio.service.GroupMessageManager;
import com.ddicar.melonradio.service.MessageGroupManager;


public class InformationDetailView extends AbstractView {


    private static final String TAG = "InformationDetailView";
    private ListView messageList;
    private GroupMessageAdapter messageAdapter;
    private TextView title;

    @Override
    public void onSwitchOff() {
        Log.e(TAG, "onSwitchOff");

    }

    @Override
    public void auto() {
        Log.e(TAG, "auto");
        RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
                ViewFlyweight.MAIN_VIEW.gotoInfoView();
            }
        });

        title = (TextView) view.findViewById(R.id.information_title);

        messageList = (ListView) view.findViewById(R.id.message_list);
        messageAdapter = new GroupMessageAdapter();
        messageList.setAdapter(messageAdapter);

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


    public void render(int position) {
        Log.e(TAG, "render");

        MessageGroupManager messageGroupManager = MessageGroupManager.getInstance();
        MessageGroup group = messageGroupManager.getMessageGroup(position);
        title.setText(group.name);

        GroupMessageManager groupMessageManager = GroupMessageManager.getInstance();
        groupMessageManager.listMessages();
    }

    public void renderMessage() {
        Log.e(TAG, "renderMessage");
        messageAdapter.render();
    }
}
