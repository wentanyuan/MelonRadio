package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ChangePasswordView extends AbstractView {

    private RelativeLayout back;
    private EditText oldPassword;
    private EditText newPassword;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        back = (RelativeLayout) view.findViewById(R.id.cancel);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.instance.switchScreen(ViewFlyweight.SETTINGS);
            }
        });


        oldPassword = (EditText) view.findViewById(R.id.old_password);

        newPassword = (EditText) view.findViewById(R.id.new_password);


        RelativeLayout confirm = (RelativeLayout) view.findViewById(R.id.button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String oldPasswordText = oldPassword.getText().toString();
                String newPasswordText = newPassword.getText().toString();

                if (StringUtil.isNullOrEmpty(oldPasswordText)) {
                    MainActivity.instance.showMessage("请输入旧密码");
                    return;
                }

                if (StringUtil.isNullOrEmpty(newPasswordText)) {
                    MainActivity.instance.showMessage("请输入新密码");
                    return;
                }

                Http http = Http.getInstance();

                http.setCallback(new SubmitCallback());

                String url = "users/update_password";

                Map<String, Object> params = new HashMap<String, Object>();

                params.put("oldPassword", oldPasswordText);
                params.put("newPassword", newPasswordText);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private class SubmitCallback implements Http.Callback {

        private final static String TAG = "SubmitCallback";

        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        MainActivity.instance.showMessage("更新密码成功。");
                        oldPassword.setText("");
                        newPassword.setText("");
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public void setWebException(WebException webException) {
            Log.e(TAG, webException.getMessage());
            MainActivity.instance.showMessage("访问服务器出现错误了");
        }
    }
}
