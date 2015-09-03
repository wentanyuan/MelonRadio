package com.ddicar.melonradio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class ChannelManager implements Callback {

	private static final String TAG = "ChannelService";

	private static ChannelManager instance = null;

	public static String[] CHANNEL_IDS = new String[] {
			"54d8278dc8b3977222b4338b",// 新闻
			"54d8279fe6867489229c3bdc",// 财经
			"54d827b33f172e9c22de3b2b",// 体育
			"54d827c3033d1faf22988f66",// 汽车
			"54d827ee02df7cc222823f02",// 科技
			"54d82839b5e55dd72245d358",// 军事
			"54d82847d9c49aec220e4184",// 娱乐
			"54d828559a309eff22b37655",// 音乐
			"54d82861ee51a4122347874b",// 游戏
			"54d8286f3fdc862523e5fa6f",// 房产
			"54d828781db544382345c9d1" };// 教育

	public boolean[] selected = new boolean[CHANNEL_IDS.length];

	List<String> channels = new ArrayList<String>();

	public static ChannelManager getInstance() {
		if (instance == null) {
			instance = new ChannelManager();
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

					for (int i = 0; i < channels.length(); i++) {
						JSONObject json = channels.getJSONObject(i);

						// System.out.println(json);

						boolean chosen = (Boolean) json.get("selected");
						String _id = (String) json.get("_id");

						for (int j = 0; j < CHANNEL_IDS.length; j++) {
							String id = CHANNEL_IDS[j];

							if (_id.equals(id)) {
								selected[j] = chosen;
							}
						}
					}

					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.DAIRY_CHANNEL);

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
