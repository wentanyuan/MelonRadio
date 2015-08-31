package com.ddicar.melonradio.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class UserManager implements Callback {

	private static UserManager instance = null;

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	private User user;

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void logout() {
		Http http = Http.getInstance();

		http.setCallback(UserManager.this);

		String url = "users/logout";

		Map<String, Object> params = new HashMap<String, Object>();

		http.post(Http.SERVER + url, params);

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
					MainActivity.instance.showMessage("已经成功退出。");
					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.LOGIN);
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

	public void refreshPhoto(Uri uri) {
		// TODO save photo to app folder
		// TODO upload photo
		// TODO update UserManager/user info
		// TODO update personalView's photo
	}
}
