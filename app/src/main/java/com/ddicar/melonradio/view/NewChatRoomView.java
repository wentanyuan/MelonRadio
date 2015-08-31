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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class NewChatRoomView extends AbstractView implements Callback {

	private static final int CLEAR_TEXT = 0x0f;
	private EditText roomName;
	private ImageView back;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {

		adjustUI();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.CHATROOMS);
			}
		});

		roomName = (EditText) view.findViewById(R.id.text_room_name);
		LinearLayout finish = (LinearLayout) view
				.findViewById(R.id.button_finish);
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String roomNameText = roomName.getText().toString();

				if (StringUtil.isNullOrEmpty(roomNameText)) {
					MainActivity.instance.showMessage("请输入房间名");
					roomName.requestFocus();
					return;
				}

				Http http = Http.getInstance();

				http.setCallback(NewChatRoomView.this);

				String url = "room/save";

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("roomname", roomNameText);

				http.post(Http.SERVER + url, params);

			}
		});
	}

	private void adjustUI() {

		TextView chatRoomsTitle = (TextView) view
				.findViewById(R.id.new_chat_room_title);
		adjustTitleBarUnitSize(chatRoomsTitle);
		adjustFontSize(chatRoomsTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		roomName = (EditText) view.findViewById(R.id.text_room_name);
		adjustFullWidth(roomName, 310);
		adjustUnitSize(roomName);
		adjustFontSize(roomName);

//		ImageView roomNameLeft = (ImageView) view
//				.findViewById(R.id.room_name_left);
//		adjustUnitSize(roomNameLeft, 13);
//
//		ImageView roomNameRight = (ImageView) view
//				.findViewById(R.id.room_name_right);
//		adjustUnitSize(roomNameRight, 13);

		TextView finishMiddle = (TextView) view
				.findViewById(R.id.finish_middle);
		adjustUnitSize(finishMiddle);
		adjustMediumFontSize(finishMiddle);
		adjustFixedWidth(finishMiddle, 300);
		

//		ImageView finishLeft = (ImageView) view.findViewById(R.id.finish_left);
//		adjustUnitSize(finishLeft, 13);
//
//		ImageView finishRight = (ImageView) view
//				.findViewById(R.id.finish_right);
//		adjustUnitSize(finishRight, 13);
	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.PLAY);

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
					MainActivity.instance.showMessage("房间创建成功。");
					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.CHATROOMS);

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
				roomName.setText("");
				break;
			}
		}
	};
}
