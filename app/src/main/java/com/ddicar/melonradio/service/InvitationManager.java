package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.GroupMessage;
import com.ddicar.melonradio.model.Invitation;
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
public class InvitationManager {

    private static final String TAG = "InvitationManager";

    private static InvitationManager instance = null;

    List<Invitation> invitations = new ArrayList<Invitation>();


    public static InvitationManager getInstance() {
        if (instance == null) {
            instance = new InvitationManager();
        }

        return instance;
    }

    public void listInvitations() {
        Log.e(TAG, "listInvitations");
        Http http = Http.getInstance();
        http.setCallback(new InvitationCallback());

        UserManager manager = UserManager.getInstance();

        String url = "users/invitations/" + manager.getUser()._id;

        Map<String, String> params = new HashMap<String, String>();

        http.get(Http.SERVER() + url, params);
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }


    private class InvitationCallback implements Http.Callback {
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
                            addInvitation(new Invitation(json));
                        }

                        ViewFlyweight.INVITATION.render();
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

    private void addInvitation(Invitation invitation) {
        Log.e(TAG,"addMessage");
        invitations.add(invitation);
    }

    private void clear() {
        Log.e(TAG,"clear");
        invitations.clear();
    }
}
