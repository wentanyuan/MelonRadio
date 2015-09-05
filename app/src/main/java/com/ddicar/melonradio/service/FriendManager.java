package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.FriendMessage;
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
public class FriendManager {


    private static final String TAG = "FriendManager";

    private static FriendManager instance = null;

    List<FriendMessage> friendMessages = new ArrayList<FriendMessage>();
    private int currentPosition;

    private FriendManager() {

    }

    public static FriendManager getInstance() {
        if (instance == null) {
            instance = new FriendManager();
        }

        return instance;
    }

    public FriendMessage getFriendMessage(int position) {
        return friendMessages.get(position);
    }

    private void addFriendMessage(FriendMessage friendMessage) {
        friendMessages.add(friendMessage);
    }

    private void clear() {
        friendMessages.clear();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public FriendMessage getCurrentFriendMessage() {
        return friendMessages.get(currentPosition);
    }


    public void listFriendMessage() {
        Log.e(TAG, "listFriendMessage");

        Http http = Http.getInstance();
        http.setCallback(new FriendMessageCallback());

        UserManager manager = UserManager.getInstance();

        String url = "users/friend_messages/" + manager.getUser()._id;

        Map<String, String> params = new HashMap<String, String>();

        http.get(Http.SERVER() + url, params);
    }

    public List<FriendMessage> getFriendMessages() {
        return friendMessages;
    }

    private class FriendMessageCallback implements Http.Callback {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        JSONArray friendMessages = (JSONArray) jsonObject.get("data");

                        clear();
                        for (int i = 0; i < friendMessages.length(); i++) {
                            JSONObject json = friendMessages.getJSONObject(i);
                            addFriendMessage(new FriendMessage(json));
                        }

                        ViewFlyweight.NEW_FRIENDS.render();
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
