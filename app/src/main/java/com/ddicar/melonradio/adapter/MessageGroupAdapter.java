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
import com.ddicar.melonradio.model.MessageGroup;
import com.ddicar.melonradio.service.MessageGroupManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/9/3.
 */
public class MessageGroupAdapter extends BaseAdapter {

    private static final String TAG = "MessageAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public MessageGroupAdapter() {
        this.mInflater = LayoutInflater.from(MainActivity.instance);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("id", String.valueOf(i));
            item.put("group", "group");
            item.put("name", "[北京-天津]运单群组");
            item.put("message", "临时有一批货物，在前方西青区普洛斯物");
            items.add(item);
        }

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.message_group_list_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(items.get(position).get("name"));

        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(items.get(position).get("time"));

        return convertView;
    }

    public void render() {
        items.clear();
        MessageGroupManager manager = MessageGroupManager.getInstance();
        List<MessageGroup> rooms = manager.getMessageGroups();

        for (int index = 0; index < rooms.size(); index++) {
            MessageGroup messageGroup = rooms.get(index);
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("id", String.valueOf(index));
            item.put("_id", messageGroup._id);
            item.put("name", messageGroup.name);
            item.put("time", messageGroup.time);
            item.put("type", messageGroup.type);
            //TODO last message.
            items.add(item);
        }

        this.notifyDataSetChanged();
    }
}
