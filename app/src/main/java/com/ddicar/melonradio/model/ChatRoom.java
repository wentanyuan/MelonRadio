package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoom {

    public ChatRoom(JSONObject json) {

        try {
            _id = json.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            roomId = json.getString("roomId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String _id;

    public String name;

    public String roomId;

}
