package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.MessageGroup;
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
public class MessageGroupManager implements Http.Callback {

    private static final String TAG = "MessageGroupManager";
    private static MessageGroupManager instance = null;

    private List<MessageGroup> messageGroups = new ArrayList<MessageGroup>();

    public static MessageGroupManager getInstance() {
        if (instance == null) {
            instance = new MessageGroupManager();
        }
        return instance;
    }

    public void listMessageGroups() {

        Http http = Http.getInstance();

        http.setCallback(MessageGroupManager.this);

        UserManager manager = UserManager.getInstance();
        String url = "users/message_groups/" + manager.getUser()._id;

        Map<String, String> params = new HashMap<String, String>();

        http.get(Http.SERVER() + url, params);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        Log.e(TAG, jsonObject.toString());

        JSONObject state;
        try {
            state = (JSONObject) jsonObject.get("state");
            if (state != null) {
                Boolean success = (Boolean) state.get("success");
                if (success) {
                    JSONArray MessageGroups = (JSONArray) jsonObject.get("data");
                    clear();
                    for (int i = 0; i < MessageGroups.length(); i++) {
                        JSONObject json = MessageGroups.getJSONObject(i);
                        addMessageGroups(new MessageGroup(json));
                    }

                    ViewFlyweight.INFO_VIEW.renderMessageGroup();


                    //TODO temporary
                    ChatRoomManager chatRoomManager = ChatRoomManager.getInstance();
                    chatRoomManager.listChatRooms();

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

    public List<MessageGroup> getMessageGroups() {
        return messageGroups;
    }

    public MessageGroup getMessageGroup(int position) {
        return messageGroups.get(position);
    }

    public void clear() {
        messageGroups.clear();
    }

    public void addMessageGroups(MessageGroup messageGroup) {
        messageGroups.add(messageGroup);
    }

}
