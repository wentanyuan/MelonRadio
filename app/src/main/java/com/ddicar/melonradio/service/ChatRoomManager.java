package com.ddicar.melonradio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class ChatRoomManager implements Callback {

	private static ChatRoomManager instance = null;

	private List<ChatRoom> rooms = new ArrayList<ChatRoom>();

	public static ChatRoomManager getInstance() {
		if (instance == null) {
			instance = new ChatRoomManager();
		}
		return instance;
	}

	public void list() {

		Http http = Http.getInstance();

		http.setCallback(ChatRoomManager.this);

		String url = "room/getList";

		Map<String, String> params = new HashMap<String, String>();

		http.get(Http.SERVER() + url, params);
	}

	@Override
	public void onResponse(JSONObject jsonObject) {

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");
			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					JSONArray data = (JSONArray) jsonObject.get("data");

					this.rooms.clear();

					// for (int i = 0; i < data.length(); i++) {
					for (int i = data.length() - 1; i >= 0; i--) {
						JSONObject json = data.getJSONObject(i);
						ChatRoom room = new ChatRoom(json);
						this.rooms.add(room);

						ViewFlyweight.CHATROOMS.refresh();
					}

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
		MainActivity.instance.showMessage("访问服务器出现错误了");
	}

	public List<ChatRoom> getRooms() {
		return rooms;
	}

	public ChatRoom getRoom(int position) {
		return rooms.get(position);
	}

	public void clear() {
		rooms.clear();
	}

	public void add(ChatRoom room) {
		rooms.add(room);
	}

}
