package com.ddicar.melonradio.service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.AddressBook;
import com.ddicar.melonradio.view.ViewFlyweight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen on 15/9/4.
 */
public class AddressBookManager {


    private static final String TAG = "AddressBookManager";

    private static AddressBookManager instance = null;

    List<AddressBook> addressBooks = new ArrayList<AddressBook>();
    private int currentPosition;

    private AddressBookManager() {

    }

    public static AddressBookManager getInstance() {
        if (instance == null) {
            instance = new AddressBookManager();
        }

        return instance;
    }

    public AddressBook getAddressBook(int position) {
        return addressBooks.get(position);
    }

    private void addAddressBook(AddressBook addressBook) {
        addressBooks.add(addressBook);
    }

    private void clear() {
        addressBooks.clear();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public AddressBook getCurrentAddressBook() {
        return addressBooks.get(currentPosition);
    }


    public void listAddressBook() {
        Log.e(TAG, "listAddressBook");


        addressBooks.clear();

        ContentResolver resolver = MainActivity.instance.getContentResolver();
        Cursor cur = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.moveToFirst()) {

            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

            do {
                //获得联系人的ID号
                String contactId = cur.getString(idColumn);
                //获得联系人姓名
                String disPlayName = cur.getString(displayNameColumn);
                //查看该联系人有多少个电话号码。如果没有这返回值为0

                int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (phoneCount > 0) {
                    //获得联系人的电话号码
                    Cursor phones = MainActivity.instance.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    if (phones.moveToFirst()) {
                        do {
                            //遍历所有的电话号码
                            String displayName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Log.e(TAG, displayName + " - " + phoneNumber);

                            AddressBook addressBook = new AddressBook(displayName, phoneNumber);
                            addressBooks.add(addressBook);

                        } while (phones.moveToNext());
                    }
                }
            } while (cur.moveToNext());

            ViewFlyweight.ADD_FROM_PHONE_BOOK.render();
        }
    }


    public List<AddressBook> getAddressBooks() {
        return addressBooks;
    }


}
