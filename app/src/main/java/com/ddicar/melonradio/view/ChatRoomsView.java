package com.ddicar.melonradio.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.GotyeChatRoomActivity;
import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.ChatRoomListAdapter;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.service.ChatRoomManager;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class ChatRoomsView extends AbstractView implements Callback {

	protected static final int REFRESH = 0x99;
	protected static final int REFRESH_PARTIALLY = 0xff;
	private ChatRoomListAdapter adapter;
	private ListView chatRooms;
	private ImageView back;
	private TextView blank;
	private ImageView search;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {

		adjustUI();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PLAY);
			}
		});

		blank = (TextView) view.findViewById(R.id.button_new);
		blank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.NEW_CHATROOM);
			}
		});

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.hideKeyboard();

				Http http = Http.getInstance();

				http.setCallback(ChatRoomsView.this);

				String url = "room/getByName/" + roomName.getText().toString();

				Map<String, String> params = new HashMap<String, String>();

				http.get(Http.SERVER + url, params);
			}
		});
		adapter = new ChatRoomListAdapter();
		chatRooms.setAdapter(adapter);
		chatRooms.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ViewFlyweight.PLAY.stop();

				ChatRoomManager manager = ChatRoomManager.getInstance();
				ChatRoom room = manager.getRoom(position);
				// MainActivity.instance.switchScreen(ViewFlyweight.CHATROOM);
				// ViewFlyweight.CHATROOM.show(room);
				// TODO 加入gotye登录和 人员list

				Intent intent = new Intent(MainActivity.instance,
						GotyeChatRoomActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", room.name);
				bundle.putString("roomId", room.roomId);
				intent.putExtras(bundle);
				MainActivity.instance.startActivity(intent);
			}
		});

		ChatRoomManager manager = ChatRoomManager.getInstance();
		manager.list();

	}

	private void adjustUI() {

		TextView chatRoomsTitle = (TextView) view
				.findViewById(R.id.chat_rooms_title);
		adjustTitleBarUnitSize(chatRoomsTitle);
		adjustFontSize(chatRoomsTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		blank = (TextView) view.findViewById(R.id.button_new);
		adjustTitleBarUnitSize(blank);
		adjustFontSize(blank);

		roomName = (EditText) view.findViewById(R.id.text_room_name);
		adjustFullWidth(roomName, 65);
		adjustUnitSize(roomName);
		adjustFontSize(roomName);

//		ImageView roomNameLeft = (ImageView) view
//				.findViewById(R.id.room_name_left);
//		adjustUnitSize(roomNameLeft, 13);
//
//		ImageView roomNameRight = (ImageView) view
//				.findViewById(R.id.room_name_right);
//		adjustUnitSize(roomNameRight, 13);

		// TextView finishMiddle = (TextView) view
		// .findViewById(R.id.finish_middle);
		// adjustUnitSize(finishMiddle);
		// adjustFontSize(finishMiddle);
		// adjustFixedWidth(finishMiddle, 130);

		chatRooms = (ListView) view.findViewById(R.id.chat_room_list);
		LayoutParams params = chatRooms.getLayoutParams();

		int[] sizes = MainActivity.instance.getWindowSize();

		// TODO titlebar's size
		params.height = sizes[1] - 100
				- AndroidUtil.pixelInPercentHeight(75f / 1280f) * 2;

		chatRooms.setLayoutParams(params);

		search = (ImageView) view.findViewById(R.id.search);
		adjustUnitSize(search);
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

	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}

	public void refresh() {

		mHandler.sendEmptyMessage(REFRESH);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message message) {
			switch (message.what) {
			case REFRESH:
				adapter.reloadItems();
				adapter.notifyDataSetChanged();
				break;

			case REFRESH_PARTIALLY:
				adapter.reloadItems();
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};
	private EditText roomName;

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

					ChatRoomManager manager = ChatRoomManager.getInstance();
					manager.clear();
					for (int i = 0; i < data.length(); i++) {
						JSONObject json = data.getJSONObject(i);
						ChatRoom room = new ChatRoom(json);
						manager.add(room);
					}//查看有几个聊天室，6/3
					mHandler.sendEmptyMessage(REFRESH_PARTIALLY);
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
