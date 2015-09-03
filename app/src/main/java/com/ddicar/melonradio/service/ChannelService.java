package com.ddicar.melonradio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class ChannelService implements Callback {

	private static final String TAG = "ChannelService";

	private static ChannelService instance = null;

	List<String> channels = new ArrayList<String>();
	
    public boolean firstRun = false;

	public static ChannelService getInstance() {
		if (instance == null) {
			instance = new ChannelService();
		}

		return instance;
	}

	public void listChannels() {

		Http http = Http.getInstance();
		http.setCallback(this);

		UserManager manager = UserManager.getInstance();

		String url = "users/getDaliyPaperSettings/" + manager.getUser()._id;

		Map<String, String> params = new HashMap<String, String>();

		// System.out.println(Http.SERVER + url);
		http.get(Http.SERVER() + url, params);
	}

	@Override
	public void onResponse(JSONObject jsonObject) {
		// System.out.println(jsonObject);

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");
			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					JSONArray channels = (JSONArray) jsonObject.get("data");

					boolean selected = false;

					for (int i = 0; i < channels.length(); i++) {
						JSONObject json = channels.getJSONObject(i);
						boolean s = (Boolean) json.get("selected");
						selected |= s;
					}

					if (selected) {

						RadioTextManager manager = RadioTextManager
								.getInstance();
						if (manager.isEmpty()) {
							Log.i(TAG, "empty");
							manager.listRadios(false);
						} else {
							Log.i(TAG, "not - empty");
							MainActivity.instance
									.switchScreenInHandler(ViewFlyweight.PLAY);
						}
					} else {
						Log.i(TAG, "channel");
						firstRun = true;
						MainActivity.instance
								.switchScreenInHandler(ViewFlyweight.DAIRY_CHANNEL);
						
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

}
