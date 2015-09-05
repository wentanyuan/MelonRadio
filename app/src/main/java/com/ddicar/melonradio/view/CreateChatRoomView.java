package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.service.ChatRoomManager;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CreateChatRoomView extends AbstractView {


    private static final String TAG = "CreateChatRoomView";
    private EditText chatRoomName;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
            }
        });


        chatRoomName = (EditText) view.findViewById(R.id.room_name);

        TextView create = (TextView) view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtil.isNullOrEmpty(chatRoomName.getText().toString())) {
                    MainActivity.instance.showMessage("请输入房间名");
                    return;
                }
                Http http = Http.getInstance();

                http.setCallback(new ChatRoomCallback());

                String url = "room/save";

                String roomNameText = chatRoomName.getText().toString();

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("roomname", roomNameText);

                http.post(Http.SERVER() + url, params);

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

    private class ChatRoomCallback implements Http.Callback {


        private static final String TAG = "ChatRoomCallback";

        @Override
        public void onResponse(JSONObject jsonObject) {

            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        JSONObject data = (JSONObject) jsonObject.get("data");

                        ChatRoomManager manager = ChatRoomManager.getInstance();
                        manager.addChatRooms(new ChatRoom(data));

                        MainActivity.instance.switchScreen(ViewFlyweight.CHAT_ROOM);

                        ViewFlyweight.CHAT_ROOM.render(manager.getRooms().size() - 1);
                    } else {
                        String message = (String) state.get("msg");
                        MainActivity.instance.showMessage(message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setWebException(WebException webException) {
            Log.e(TAG, "setWebException");
            MainActivity.instance.showMessage("访问服务器出现错误了");
        }
    }
}
