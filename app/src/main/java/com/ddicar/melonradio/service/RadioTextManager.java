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

public class RadioTextManager implements Callback {

	private static RadioTextManager instance = null;

	private List<RadioText> radioTexts = new ArrayList<RadioText>();

	private int current;

	private boolean refresh;

	public static RadioTextManager getInstance() {
		if (instance == null) {
			instance = new RadioTextManager();
		}

		return instance;
	}

	public RadioText prev() {

		if (current > 0) {
			current--;
		} else {
			return null;
		}
		if (radioTexts.size() > current) {
			return radioTexts.get(current);
		}
		return null;
	}

	public RadioText next() {
		if (current < radioTexts.size() - 1) {
			current++;
		} else {
			return null;
		}
		if (radioTexts.size() > current) {
			return radioTexts.get(current);
		}
		return null;
	}

	public List<RadioText> search(String keyword) {
		return null;
	}

	public void listRadios(boolean refresh) {
		this.refresh = refresh;

		Http http = Http.getInstance();
		http.setCallback(this);

		UserManager manager = UserManager.getInstance();

		String url = "users/daliyPaper/showAll/" + manager.getUser()._id + "/1";

		Map<String, String> params = new HashMap<String, String>();

		http.get(Http.SERVER + url, params);

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
						ViewFlyweight.DAIRY.refresh();
					}

					if(!refresh) {
						MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.PLAY);
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
		return radioTexts.isEmpty();
	}

	public List<RadioText> getRadioTexts() {
		return radioTexts;
	}

	public void setRadioTexts(List<RadioText> radioTexts) {
		this.radioTexts = radioTexts;
	}

	public RadioText getCurrentRadioText() {
		if (radioTexts.size() > current) {
			return radioTexts.get(current);
		}
		return null;
	}

	public void setCurrent(int position) {
		current = position;

	}
	
	public void updateFavorite(int position, int count) {
		radioTexts.get(position).favorites = count;
	}

	public void setCurrent(String _id) {
		for (int pos = 0; pos < radioTexts.size(); pos++) {
			RadioText radio = radioTexts.get(pos);
			if (radio._id.equals(_id)) {
				current = pos;
			}
		}
	}
}
