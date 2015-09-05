package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen on 15/9/4.
 */
public class MessageGroup {

    public String _id;

    public String time;

    public String lastMessage;

    public String name;

    public String type;


    //1 group, 2 invitation of add friend 3 invitation of chat

    public MessageGroup(JSONObject json) {

        try {
            _id = json.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            time = json.getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            lastMessage = json.getString("lastMessage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            type = json.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
