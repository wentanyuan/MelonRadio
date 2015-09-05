package com.ddicar.melonradio.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.FriendMessage;
import com.ddicar.melonradio.service.FriendManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/8/31.
 */
public class FriendMessageAdapter extends BaseAdapter {


    private static final String TAG = "FriendAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public FriendMessageAdapter() {
        this.mInflater = LayoutInflater.from(MainActivity.instance);

//        for (int i = 0; i < 5; i++) {
//            HashMap<String, String> item = new HashMap<String, String>();
//            item.put("id", String.valueOf(i));
//            item.put("name", "黄宇");
//            item.put("message", "我是黄宇");
//            item.put("accepted", "false");
//            items.add(item);
//        }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.friend_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(items.get(position).get("name"));

        TextView message = (TextView) convertView.findViewById(R.id.message);
        message.setText(items.get(position).get("message"));


        RelativeLayout accept = (RelativeLayout) convertView.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.showMessage("添加朋友成功");

                //TODO http...

                items.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void render() {
        FriendManager friendManager = FriendManager.getInstance();

        List<FriendMessage> friendMessages = friendManager.getFriendMessages();

        items.clear();
        for (int i = 0; i < friendMessages.size(); i++) {
            FriendMessage friendMessage = friendMessages.get(i);
            HashMap<String, String> item = new HashMap<String, String>();

            item.put("id", "" + i);
            item.put("_id", friendMessage._id);
            item.put("name", friendMessage.name);
            item.put("message", friendMessage.message);
            item.put("accepted", friendMessage.accepted);

            items.add(item);

        }

        notifyDataSetChanged();
    }
}
