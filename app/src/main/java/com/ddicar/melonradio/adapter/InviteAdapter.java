package com.ddicar.melonradio.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.Invite;
import com.ddicar.melonradio.service.InviteManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/8/31.
 */
public class InviteAdapter extends BaseAdapter {


    private static final String TAG = "InviteAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public InviteAdapter() {
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
        convertView = mInflater.inflate(R.layout.contact_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(items.get(position).get("hostName"));

        return convertView;
    }

    public void render() {
        InviteManager inviteManager = InviteManager.getInstance();
        List<Invite> Invites = inviteManager.getInvites();

        items.clear();

        for (int i = 0; i < Invites.size(); i++) {
            Invite Invite = Invites.get(i);

            HashMap<String, String> item = new HashMap<String, String>();

            item.put("id", String.valueOf(i));
            item.put("_id", Invite._id);
            item.put("hostId", Invite.hostId);
            item.put("hostAvatar", Invite.hostAvatar);
            item.put("hostName", Invite.hostName);
            item.put("chatRoomId", Invite.chatRoomId);
            item.put("roomName", Invite.roomName);
            item.put("accepted", String.valueOf(Invite.accepted));

            items.add(item);
        }

        notifyDataSetChanged();

    }
}
