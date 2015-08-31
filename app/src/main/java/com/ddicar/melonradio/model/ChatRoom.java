package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoom {

	public ChatRoom(JSONObject json) {

		try {
			_id = json.getString("_id");
			roomId = json.getString("roomId");
			name = json.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public String _id;

	public String name;

	public String roomId;

}
