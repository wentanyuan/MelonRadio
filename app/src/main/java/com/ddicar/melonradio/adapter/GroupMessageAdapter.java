package com.ddicar.melonradio.adapter;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.model.GroupMessage;
import com.ddicar.melonradio.service.ChatRoomManager;
import com.ddicar.melonradio.service.GroupMessageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/9/3.
 */
public class GroupMessageAdapter extends BaseAdapter {

    private static final String TAG = "GroupMessageAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public GroupMessageAdapter() {
        this.mInflater = LayoutInflater.from(MainActivity.instance);
    }


    @Override
    public int getCount() {
        Log.e(TAG, "getCount");
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        Log.e(TAG, "getItem");
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.e(TAG, "getItemId");
        return Long.parseLong(items.get(position).get("id"));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.group_message_item, null);

        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(items.get(position).get("content"));

        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(items.get(position).get("time"));

        return convertView;
    }

    public void render() {
        Log.e(TAG, "render");
        items.clear();
        GroupMessageManager manager = GroupMessageManager.getInstance();
        List<GroupMessage> groupMessages = manager.getGroupMessages();

        for (int index = 0; index < groupMessages.size(); index++) {
            Log.e(TAG, "add item");
            GroupMessage message = groupMessages.get(index);
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("id", String.valueOf(index));
            item.put("_id", message._id);
            item.put("content", message.content);
            item.put("time", message.time);
            items.add(item);
        }
        notifyDataSetChanged();
    }
}
