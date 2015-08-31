package com.ddicar.melonradio.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class FeedbackView extends AbstractView implements Callback {

	private static final int CLEAR_TEXT = 0x0f;
	private ImageView back;
	private TextView send;
	private EditText content;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance
						.switchScreen(ViewFlyweight.SOFTWARE_SETTING);
			}
		});

		content = (EditText) view.findViewById(R.id.feed_back_text);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Http http = Http.getInstance();

				http.setCallback(FeedbackView.this);

				String url = "users/feedback";

				UserManager manager = UserManager.getInstance();

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("user_id", manager.getUser()._id);
				params.put("content", content);

				http.post(Http.SERVER + url, params);
			}
		});

	}

	private void adjustUI() {
		TextView feedbackTitle = (TextView) view
				.findViewById(R.id.feedback_title);
		adjustTitleBarUnitSize(feedbackTitle);
		adjustFontSize(feedbackTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		send = (TextView) view.findViewById(R.id.button_send);
		adjustTitleBarUnitSize(send);
		adjustFontSize(send);

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.SOFTWARE_SETTING);

	}

	@Override
	public void onTouch(MotionEvent event) {
		MainActivity.instance.hideKeyboard();
	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
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
					MainActivity.instance.showMessage("您的反馈发送成功，非常感谢。");

					mHandler.sendEmptyMessage(CLEAR_TEXT);
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

	Handler mHandler = new Handler() {

		public void handleMessage(Message message) {
			switch (message.what) {
			case CLEAR_TEXT:
				content.setText("");
				MainActivity.instance
						.switchScreenInHandler(ViewFlyweight.SOFTWARE_SETTING);
				break;
			}
		}
	};

}
