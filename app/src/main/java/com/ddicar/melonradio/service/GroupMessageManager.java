package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.GroupMessage;
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
public class GroupMessageManager {

    private static final String TAG = "GroupMessageManager";

    private static GroupMessageManager instance = null;

    List<GroupMessage> messages = new ArrayList<GroupMessage>();


    public static GroupMessageManager getInstance() {
        if (instance == null) {
            instance = new GroupMessageManager();
        }

        return instance;
    }

    public void listMessages() {
        Log.e(TAG,"listMessages");
        Http http = Http.getInstance();
        http.setCallback(new MessageCallback());

        UserManager manager = UserManager.getInstance();

        String url = "users/invitations/" + manager.getUser()._id;

        Map<String, String> params = new HashMap<String, String>();

        http.get(Http.SERVER() + url, params);
    }

    public List<GroupMessage> getGroupMessages() {
        return messages;
    }


    private class MessageCallback implements Http.Callback {
        @Override
        public void onResponse(JSONObject jsonObject) {

            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        JSONArray messages = (JSONArray) jsonObject.get("data");

                        clear();
                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject json = messages.getJSONObject(i);
                            addMessage(new GroupMessage(json));
                        }

                        ViewFlyweight.INFORMATION_DETAIL_VIEW.renderMessage();
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
            MainActivity.instance.showMessage("访问服务器出现错误了");

        }
    }

    private void addMessage(GroupMessage message) {
        Log.e(TAG,"addMessage");
        messages.add(message);
    }

    private void clear() {
        Log.e(TAG,"clear");
        messages.clear();
    }
}
