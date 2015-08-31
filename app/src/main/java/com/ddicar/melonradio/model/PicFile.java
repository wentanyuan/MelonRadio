package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PicFile {
	private static final String TAG = "PicFile";

	public PicFile(JSONObject json) {

		try {
			name = json.getString("name");
			fileName = json.getString("fileName");
			mimeType = "image/jpg";
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}

	}

	public PicFile() {
	}

	public String fileName;

	public String name;

	public String mimeType;
}
