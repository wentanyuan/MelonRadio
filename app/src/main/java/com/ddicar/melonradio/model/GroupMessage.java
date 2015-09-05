package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen on 15/9/4.
 */
public class GroupMessage {

    public String _id;

    public String content;

    public String time;

    public GroupMessage(JSONObject json) {

        try {
            _id = json.getString("_id");
            content = json.getString("content");
            time = json.getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
