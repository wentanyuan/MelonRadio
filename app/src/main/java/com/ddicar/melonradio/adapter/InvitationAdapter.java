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
import com.ddicar.melonradio.model.Invitation;
import com.ddicar.melonradio.service.InvitationManager;
import com.ddicar.melonradio.view.ViewFlyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/8/31.
 */
public class InvitationAdapter extends BaseAdapter {


    private static final String TAG = "PhoneBookAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public InvitationAdapter() {
        this.mInflater = LayoutInflater.from(MainActivity.instance);

//        for (int i = 0; i < 5; i++) {
//            HashMap<String, String> item = new HashMap<String, String>();
//            item.put("id", String.valueOf(i));
//            item.put("name", "黄宇");
//            item.put("message", "我是黄宇");
//            item.put("attend", "false");
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.invitation_item, null);

        TextView hostName = (TextView) convertView.findViewById(R.id.host_name);
        hostName.setText(items.get(position).get("hostName"));

        TextView roomName = (TextView) convertView.findViewById(R.id.chat_room_name);
        roomName.setText(items.get(position).get("roomName"));

        RelativeLayout attend = (RelativeLayout) convertView.findViewById(R.id.attend);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.CHAT_ROOM);
                //TODO render that room.
            }
        });

        return convertView;
    }

    public void render() {

        InvitationManager invitationManager = InvitationManager.getInstance();
        List<Invitation> invitations = invitationManager.getInvitations();

        items.clear();

        for (int i = 0; i < invitations.size(); i++) {
            Invitation invitation = invitations.get(i);

            HashMap<String, String> item = new HashMap<String, String>();

            item.put("id", String.valueOf(i));
                    item.put("_id", invitation._id);
            item.put("hostId", invitation.hostId);
            item.put("hostAvatar", invitation.hostAvatar);
            item.put("hostName", invitation.hostName);
            item.put("chatRoomId", invitation.chatRoomId);
            item.put("roomName", invitation.roomName);
            item.put("accepted", String.valueOf(invitation.accepted));

            items.add(item);
        }

        notifyDataSetChanged();

    }
}
