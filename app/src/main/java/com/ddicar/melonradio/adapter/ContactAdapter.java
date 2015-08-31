package com.ddicar.melonradio.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.view.ViewFlyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/8/31.
 */
public class ContactAdapter extends BaseAdapter {


    private static final String TAG = "ContactAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public ContactAdapter() {
        this.mInflater = LayoutInflater.from(MainActivity.instance);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("id", String.valueOf(i));
            item.put("name", "黄宇");
            item.put("phone", "13210230098");
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
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.contact_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(items.get(position).get("name"));
//
//        TextView phone = (TextView) convertView.findViewById(R.id.phone);
//        phone.setText(items.get(position).get("phone"));
//
//
//        RelativeLayout add = (RelativeLayout) convertView.findViewById(R.id.add);
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.instance.switchScreen(ViewFlyweight.VERIFICATION);
//            }
//        });

        return convertView;
    }

}
