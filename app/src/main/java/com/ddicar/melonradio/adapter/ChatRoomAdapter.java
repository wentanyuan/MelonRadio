package com.ddicar.melonradio.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.service.ChatRoomManager;
import com.ddicar.melonradio.util.AndroidUtil;

public class ChatRoomAdapter extends BaseAdapter {

	private static final String TAG = "ChatRoomAdapter";
	private LayoutInflater mInflater;
	List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

	public ChatRoomAdapter() {
		this.mInflater = LayoutInflater.from(MainActivity.instance);


		for (int i = 0; i < 5; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("id", String.valueOf(i));
			item.put("name", "聊天");
			items.add(item);
		}
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(items.get(position).get("id"));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.chat_room_item, null);
		TextView name = (TextView) convertView
				.findViewById(R.id.name);
		name.setText(items.get(position).get("name"));

		return convertView;
	}

	public void render() {
		Log.e(TAG, "render");
		items.clear();
		ChatRoomManager manager = ChatRoomManager.getInstance();
		List<ChatRoom> rooms = manager.getRooms();

		for (int index = 0; index < rooms.size(); index++) {
			ChatRoom room = rooms.get(index);
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("id", String.valueOf(index));
			item.put("_id", room._id);
			item.put("name", room.name);
			items.add(item);
		}
		this.notifyDataSetChanged();
	}
}
