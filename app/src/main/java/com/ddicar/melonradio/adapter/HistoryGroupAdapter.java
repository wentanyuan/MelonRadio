package com.ddicar.melonradio.adapter;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class HistoryGroupAdapter extends BaseAdapter {

    private static final String TAG = "HistoryGroupAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public HistoryGroupAdapter() {
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
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.history_group_item, null);

        ImageView group = (ImageView) convertView.findViewById(R.id.group);
//        group.setText(items.get(position).get("name"));
        //TODO render avatar.

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(items.get(position).get("name"));

        TextView lastMessage = (TextView) convertView.findViewById(R.id.last_message);
        lastMessage.setText(items.get(position).get("lastMessage"));

        TextView time = (TextView) convertView.findViewById(R.id.time);
        time.setText(items.get(position).get("time"));

        return convertView;
    }

    public void render() {
        Log.e(TAG, "render");
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
            item.put("lastMessage", messageGroup.lastMessage);
            items.add(item);
        }

        this.notifyDataSetChanged();
    }
}
