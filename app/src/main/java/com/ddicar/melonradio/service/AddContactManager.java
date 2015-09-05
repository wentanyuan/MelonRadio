package com.ddicar.melonradio.service;

import android.util.Log;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.model.AddressBook;
import com.ddicar.melonradio.model.ChatRoom;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Stephen on 15/9/4.
 */
public class AddContactManager {

    private static final String TAG = "AddContactManager";

    private static AddContactManager instance = null;

    AddressBook addressBook;

    private AddContactManager() {

    }

    public static AddContactManager getInstance() {
        if (instance == null) {
            instance = new AddContactManager();
        }

        return instance;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void sendInvitation(String text) {

        if(StringUtil.isNullOrEmpty(text)) {
            MainActivity.instance.showMessage("请输入消息");
            return;
        }

        Http http = Http.getInstance();

        http.setCallback(new SendInvitationCallback());

        String url = "users/send_invitation";

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("displayName", addressBook.displayName);
        params.put("phoneNumber", addressBook.phoneNumber);
        params.put("message", text);

        http.post(Http.SERVER() + url, params);
    }

    private class SendInvitationCallback implements Http.Callback {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, jsonObject.toString());
            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        JSONObject data = (JSONObject) jsonObject.get("data");

                        MainActivity.instance.switchScreen(ViewFlyweight.ADD_FROM_PHONE_BOOK);
                        MainActivity.instance.showMessage("消息发送成功");

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
