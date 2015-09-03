package com.ddicar.melonradio.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.ChatRoomAdapter;
import com.ddicar.melonradio.adapter.MessageAdapter;
import com.ddicar.melonradio.util.AndroidUtil;


public class InformationView extends AbstractView {


    private static final String TAG = "InformationView";
    private RelativeLayout dialog;
    private ListView messages;
    private ListView chatRooms;
    private RelativeLayout infoBackground;
    private TextView infoText;
    private RelativeLayout chatRoomBackground;
    private TextView chatRoomText;

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

        messages = (ListView) view.findViewById(R.id.messages);
        messages.setAdapter(new MessageAdapter());
        messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "information_list clicked");
                MainActivity.instance.switchScreen(ViewFlyweight.INFORMATION_DETAIL_VIEW);
            }
        });


        infoBackground = (RelativeLayout)view.findViewById(R.id.info_tab_background);
        infoText = (TextView)view.findViewById(R.id.info_tab_text);
        chatRoomBackground = (RelativeLayout)view.findViewById(R.id.chat_tab_background);
        chatRoomText = (TextView)view.findViewById(R.id.chat_tab_text);

        RelativeLayout infoTab = (RelativeLayout) view.findViewById(R.id.info_tab);

        infoTab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                messages.setVisibility(View.VISIBLE);
                chatRooms.setVisibility(View.INVISIBLE);
                infoBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.left_tab_selected));
                infoText.setTextColor(AndroidUtil.parseColor(R.color.dark_text));
                chatRoomBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.right_tab_unselected));
                chatRoomText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
            }
        });



        chatRooms = (ListView) view.findViewById(R.id.chat_rooms);
        chatRooms.setAdapter(new ChatRoomAdapter());
        chatRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "chat_room_list clicked");
                MainActivity.instance.switchScreen(ViewFlyweight.CHAT_ROOM);
            }
        });


        RelativeLayout chatTab = (RelativeLayout) view.findViewById(R.id.chat_tab);

        chatTab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                messages.setVisibility(View.INVISIBLE);
                chatRooms.setVisibility(View.VISIBLE);
                infoBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.left_tab_unselected));
                infoText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
                chatRoomBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.right_tab_selected));
                chatRoomText.setTextColor(AndroidUtil.parseColor(R.color.dark_text));
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
