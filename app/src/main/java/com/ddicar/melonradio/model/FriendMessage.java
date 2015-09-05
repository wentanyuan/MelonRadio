package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen on 15/9/4.
 */
public class FriendMessage {
    public String name;
    public String message;
    public String accepted;
    public String _id;

    public FriendMessage(JSONObject json) {

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
            message = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            accepted = json.getString("accepted");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
