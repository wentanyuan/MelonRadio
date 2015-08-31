package com.ddicar.melonradio.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.RadioText;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class FavoriteService implements Callback {

	private static FavoriteService instance = null;

	public static FavoriteService getInstance() {
		if (instance == null) {
			instance = new FavoriteService();
		}

		return instance;
	}

	public void favorite(RadioText radioText) {

		Http http = Http.getInstance();
		http.setCallback(this);

		UserManager manager = UserManager.getInstance();

		String url = "users/favorite/";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", manager.getUser()._id);
		params.put("post_id", radioText._id);

		http.post(Http.SERVER + url, params);
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
					MainActivity.instance.showMessage("收藏成功。");
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
