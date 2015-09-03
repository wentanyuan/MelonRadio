package com.ddicar.melonradio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.RadioText;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class FavoriteManager implements Callback {

	private static FavoriteManager instance = null;

	private List<RadioText> radioTexts = new ArrayList<RadioText>();

	public static FavoriteManager getInstance() {
		if (instance == null) {
			instance = new FavoriteManager();
		}

		return instance;
	}

	public void listFavorite() {

		Http http = Http.getInstance();
		http.setCallback(this);

		UserManager manager = UserManager.getInstance();

		String url = "users/favorite/showAll/" + manager.getUser()._id;

		Map<String, String> params = new HashMap<String, String>();

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

					Object obj = jsonObject.get("data");

					if (obj instanceof JSONArray) {
						JSONArray data = (JSONArray) obj;

						this.radioTexts.clear();

						for (int i = 0; i < data.length(); i++) {
							JSONObject json = data.getJSONObject(i);
							RadioText radioTexts = new RadioText(json);
							this.radioTexts.add(radioTexts);
						}
						ViewFlyweight.FAVORITE.refresh();
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

	public List<RadioText> getRadioTexts() {
		return radioTexts;
	}

	public void removeAt(int position) {
		radioTexts.remove(position);
	}

}
