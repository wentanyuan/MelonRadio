package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RadioText {

	public String pic;

	public String title;

	public String content;

	public String author;

	public String contentType;

	public String channel;

	public int favorites;

	public boolean favorited;

	public String _id;

	public RadioText(JSONObject data) {
		try {
			_id = (String) data.get("_id");
			pic = (String) data.get("pic");
			title = (String) data.get("title");
			content = (String) data.get("content");
			author = (String) data.get("author");
			contentType = (String) data.get("contentType");
			favorites = (Integer) data.get("favorites");
			favorited = (Boolean) data.get("favorited");
			channel = (String) data.get("channel");
		} catch (JSONException e) {
			 e.printStackTrace();
		}
	}
}
