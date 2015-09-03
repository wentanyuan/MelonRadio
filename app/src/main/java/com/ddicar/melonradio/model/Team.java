package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen on 15/9/3.
 */
public class Team {

    public String name;

    public Team(JSONObject json) {
        try {
            name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
