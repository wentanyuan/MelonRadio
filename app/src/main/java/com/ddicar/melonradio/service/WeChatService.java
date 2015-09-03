package com.ddicar.melonradio.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

public class WeChatService implements Http.Callback {

	private static final String TAG = "WeChatService";

	private static WeChatService instance = null;
	private User user = null;

	private WeChatService() {
	}

	public static WeChatService getInstance() {
		if (instance == null) {
			instance = new WeChatService();
		}
		return instance;
	}

	public void login(String code, String token, User user) {
		Log.e(TAG, "login");
		this.user = user;
		Log.e(TAG, user.pic);
		Log.e(TAG, user.name);

		String url = "users/wxauth/" + token + "/" + code;

		Http http = Http.getInstance();

		http.setCallback(WeChatService.this);

		Map<String, String> params = new HashMap<String, String>();

		http.get(Http.SERVER() + url, params);

	}

	@Override
	public void onResponse(JSONObject jsonObject) {
		Log.e(TAG, jsonObject.toString());

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");
			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					JSONObject data = (JSONObject) jsonObject.get("data");

					User user = new User(data);
					Log.e(TAG, this.user.pic);
					Log.e(TAG, this.user.name);
					Log.e(TAG, this.user.sex);
					try {
						user.pic = this.user.pic;
						user.name = this.user.name;
						user.sex = this.user.sex;
						user.phone = this.user.phone;
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}

					UserManager mgr = UserManager.getInstance();
					
//					if(user._id.length()>2){
//						user._id = user._id.substring(0, user._id.length()-1)+"21";
//						
//					}
					
					mgr.setUser(user);
					
					//Toast.makeText(MainActivity.instance, "微信登录时的UserId="+mgr.getUser()._id, Toast.LENGTH_SHORT).show();
					//Toast.makeText(MainActivity.instance, "微信登录时的UserName"+mgr.getUser().name, Toast.LENGTH_SHORT).show();
					// System.out.println(data);
//					MainActivity.instance
//							.switchScreenInHandler(ViewFlyweight.PLAY);
					
					ChannelService service = ChannelService.getInstance();
					service.listChannels();

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
