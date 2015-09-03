package com.ddicar.melonradio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.History;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class HistoryManager implements Callback {

	private static HistoryManager instance = null;

	private List<History> histories = new ArrayList<History>();

	public static HistoryManager getInstance() {
		if (instance == null) {
			instance = new HistoryManager();
		}

		return instance;
	}

	public void listHistories() {

		Http http = Http.getInstance();
		http.setCallback(this);

		UserManager manager = UserManager.getInstance();

		String url = "users/getWarnings/" + manager.getUser()._id;

		Map<String, String> params = new HashMap<String, String>();

		http.get(Http.SERVER() + url, params);

	}

	@Override
	public void onResponse(JSONObject jsonObject) {
		System.out.println(jsonObject);

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");
			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					JSONArray data = (JSONArray) jsonObject.get("data");

					this.histories.clear();

					for (int i = 0; i < data.length(); i++) {
						JSONObject json = data.getJSONObject(i);
						History history = new History(json);
						this.histories.add(history);

						ViewFlyweight.HISTORY.refresh();
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

	public boolean isEmpty() {
		return histories.isEmpty();
	}

	public List<History> getHistories() {
		return histories;
	}

}
