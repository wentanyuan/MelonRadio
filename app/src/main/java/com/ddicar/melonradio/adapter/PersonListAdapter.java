package com.ddicar.melonradio.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.AndroidUtil;

public class PersonListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

	public PersonListAdapter() {
		this.mInflater = LayoutInflater.from(MainActivity.instance);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "1");
		map.put("name", "name");
		items.add(map);
		items.add(map);
		items.add(map);
		items.add(map);
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
		convertView = mInflater.inflate(R.layout.history_item, null);

		TextView name = (TextView) convertView.findViewById(R.id.issue_title);
		name.setText(items.get(position).get("name"));
		name.setTextSize(AndroidUtil.dependentFontSize(22));

		return convertView;
	}

}
