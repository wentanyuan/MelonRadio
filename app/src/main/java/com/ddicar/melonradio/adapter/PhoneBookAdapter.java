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
import com.ddicar.melonradio.model.AddressBook;
import com.ddicar.melonradio.service.AddContactManager;
import com.ddicar.melonradio.service.AddressBookManager;
import com.ddicar.melonradio.view.ViewFlyweight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 15/8/31.
 */
public class PhoneBookAdapter extends BaseAdapter {


    private static final String TAG = "PhoneBookAdapter";
    private LayoutInflater mInflater;
    List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

    public PhoneBookAdapter() {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        convertView = mInflater.inflate(R.layout.phone_book_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(items.get(position).get("displayName"));

        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        phone.setText(items.get(position).get("phoneNumber"));

        RelativeLayout add = (RelativeLayout) convertView.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressBookManager addressBookManager =  AddressBookManager.getInstance();

                AddContactManager addContactManager =  AddContactManager.getInstance();
                addContactManager.setAddressBook(addressBookManager.getAddressBook(position));

                MainActivity.instance.switchScreen(ViewFlyweight.VERIFICATION);

            }
        });

        return convertView;
    }

    public void render() {
        Log.e(TAG, "render");
        AddressBookManager manager = AddressBookManager.getInstance();

        List<AddressBook> addressBooks = manager.getAddressBooks();

        items.clear();

        for (int i = 0; i < addressBooks.size(); i++) {

            Log.e(TAG, "add address book");
            HashMap<String, String> item = new HashMap<String, String>();

            AddressBook addressBook = addressBooks.get(i);
            item.put("id", "" + i);
            item.put("displayName", addressBook.displayName);
            item.put("phoneNumber", addressBook.phoneNumber);

            items.add(item);
        }

        notifyDataSetChanged();
    }
}
