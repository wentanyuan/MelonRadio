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
import com.ddicar.melonradio.model.History;
import com.ddicar.melonradio.service.HistoryManager;
import com.ddicar.melonradio.util.AndroidUtil;

public class HistoryListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

	public HistoryListAdapter() {
		this.mInflater = LayoutInflater.from(MainActivity.instance);
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

	public void reloadItems() {
		items.clear();
		HistoryManager manager = HistoryManager.getInstance();
		List<History> histories = manager.getHistories();

		for (int index = 0; index < histories.size(); index++) {
			History history = histories.get(index);
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("id", String.valueOf(index));

			item.put("_id", history._id);

			// TODO update UI.
		}

	}

}
