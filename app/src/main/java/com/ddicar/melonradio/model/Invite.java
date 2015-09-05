package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen on 15/9/5.
 */
public class Invite {

    public String _id;

    public String hostId;

    public String chatRoomId;

    public String hostName;

    public String roomName;

    public String hostAvatar;

    public Boolean accepted;

    public Invite(JSONObject json) {

        try {
            _id = json.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            hostId = json.getString("hostId");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            chatRoomId = json.getString("chatRoomId");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            hostName = json.getString("hostName");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            roomName = json.getString("roomName");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            hostAvatar = json.getString("hostAvatar");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            accepted = json.getBoolean("accepted");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
