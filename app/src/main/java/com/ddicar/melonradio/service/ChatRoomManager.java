package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomManager implements Callback {

    private static final String TAG = "ChatRoomManager";
    private static ChatRoomManager instance = null;


    int current ;

    private List<ChatRoom> rooms = new ArrayList<ChatRoom>();

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    public void listChatRooms() {

        Http http = Http.getInstance();

        http.setCallback(ChatRoomManager.this);

        String url = "room/getList";

        Map<String, String> params = new HashMap<String, String>();

        http.get(Http.SERVER() + url, params);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        Log.e(TAG, jsonObject.toString());
        JSONObject state;
        try {
            state = (JSONObject) jsonObject.get("state");
            if (state != null) {
                Boolean success = (Boolean) state.get("success");
                if (success) {
                    JSONArray chatRooms = (JSONArray) jsonObject.get("data");
                    clear();
                    for (int i = 0; i < chatRooms.length(); i++) {
                        JSONObject json = chatRooms.getJSONObject(i);
                        addChatRooms(new ChatRoom(json));
                    }

                    ViewFlyweight.INFO_VIEW.renderChatRoom();

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

    public List<ChatRoom> getRooms() {
        Log.e(TAG, "getRooms");
        return rooms;
    }

    public ChatRoom getRoom(int position) {
        Log.e(TAG, "getRoom");
        return rooms.get(position);
    }

    public void clear() {
        Log.e(TAG, "clear");
        rooms.clear();
    }

    public void addChatRooms(ChatRoom room) {
        Log.e(TAG, "addChatRooms");
        rooms.add(room);
    }

    public void setCurrent(int current) {
        this.current = current;
    }

}
