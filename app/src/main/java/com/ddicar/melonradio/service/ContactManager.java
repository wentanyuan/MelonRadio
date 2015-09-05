package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 15/9/4.
 */
public class ContactManager {

    private static final String TAG = "ContactManager";

    private static ContactManager instance = null;

    List<User> contacts = new ArrayList<User>();
    private int currentPosition;

    private ContactManager() {

    }

    public static ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
        }

        return instance;
    }

    public User getUser(int position) {
        return contacts.get(position);
    }

    private void addContacts(User user) {
        contacts.add(user);
    }

    private void clear() {
        contacts.clear();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public User getCurrentContact() {
        return contacts.get(currentPosition);
    }


    public void listContacts() {
        Log.e(TAG, "listContacts");

        Http http = Http.getInstance();
        http.setCallback(new ContactCallback());

        UserManager manager = UserManager.getInstance();

        String url = "users/contacts/" + manager.getUser()._id;

        Map<String, String> params = new HashMap<String, String>();

        http.get(Http.SERVER() + url, params);
    }

    public List<User> getContacts() {
        return contacts;
    }

    private class ContactCallback implements Http.Callback {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        JSONArray contacts = (JSONArray) jsonObject.get("data");

                        clear();
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject json = contacts.getJSONObject(i);
                            addContacts(new User(json));
                        }

                        ViewFlyweight.CONTACT_LIST_VIEW.render();
                    } else {
                        String message = (String) state.get("msg");
                        MainActivity.instance.showMessage(message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void setWebException(WebException webException) {
            Log.e(TAG, "setWebException");
            MainActivity.instance.showMessage("访问服务器出现错误了");
        }
    }

}
