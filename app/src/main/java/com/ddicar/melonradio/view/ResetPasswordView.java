package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stephen on 15/8/30.
 */
public class ResetPasswordView extends AbstractView {

    private EditText oldPassword;
    private EditText newPassword;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {


        oldPassword = (EditText) view.findViewById(R.id.old_password);
        newPassword = (EditText) view.findViewById(R.id.old_password);

        RelativeLayout reset = (RelativeLayout) view.findViewById(R.id.button_reset_password);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager userManager = UserManager.getInstance();

                Http http = Http.getInstance();

                http.setCallback(new ResetPasswordCallback());

                String url = "users/reset_password";

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("old_password", oldPassword.getText().toString());
                params.put("new_password", newPassword.getText().toString());
                params.put("phone", userManager.getUser().phone);

                http.post(Http.SERVER() + url, params);
            }
        });

    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    private class ResetPasswordCallback implements Http.Callback {
        private static final String TAG = "ResetPasswordCallback";

        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        MainActivity.instance.showMessage("密码变更成功");
                        MainActivity.instance.switchScreenInHandler(ViewFlyweight.LOGIN);
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
            Log.e(TAG, webException.getMessage());
            MainActivity.instance.showMessage("访问服务器出现错误了");

        }
    }
}
