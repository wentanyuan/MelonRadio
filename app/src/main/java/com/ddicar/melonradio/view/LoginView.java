package com.ddicar.melonradio.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.NetWorkStatus;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;
import com.gotye.api.voichannel.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

public class LoginView extends AbstractView implements Callback {
    private static final String TAG = "LoginView";

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private static final int CLEAR_TEXT = 0x0f;
    private EditText userName;
    private EditText password;
    private TextView forgetPassword;


    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        // call sharesdk
        ShareSDK.initSDK(MainActivity.instance);

        adjustUI();

        NetWorkStatus netWorkStatus = NetWorkStatus.getInstance();

        boolean isOpenNetwork = netWorkStatus.isOpenNetwork();

        if (!isOpenNetwork) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.instance);
            builder.setTitle("没有可用的网络").setMessage("是否对网络进行设置?");

            builder.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = null;

                            try {
                                String sdkVersion = android.os.Build.VERSION.SDK;
                                if (Integer.valueOf(sdkVersion) > 10) {
                                    intent = new Intent(
                                            android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    ComponentName comp = new ComponentName(
                                            "com.android.settings",
                                            "com.android.settings.WirelessSettings");
                                    intent.setComponent(comp);
                                    intent.setAction("android.intent.action.VIEW");
                                }
                                MainActivity.instance.startActivity(intent);
                            } catch (Exception e) {
                                Log.w(TAG,
                                        "open network settings failed, please check...");
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(
                            "否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();

        }

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.e(TAG, "login onClick()");

                MainActivity.instance.hideKeyboard();

                String userNameText = userName.getText().toString();
                String passwordText = password.getText().toString();

                if (StringUtil.isNullOrEmpty(userNameText)) {
                    MainActivity.instance.showMessage("请输入账号");
                    userName.requestFocus();
                    return;
                }

                if (StringUtil.isNullOrEmpty(passwordText)) {
                    MainActivity.instance.showMessage("请输入密码");
                    password.requestFocus();
                    return;
                }

//                Http http = Http.getInstance();
//
//                http.setCallback(new LoginCallback());
//
//                String url = "users/login";
//
//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("phone", userNameText);
//                params.put("password", passwordText);
//
//                http.post(Http.SERVER + url, params);


                UserManager user = UserManager.getInstance();
                user.setUser(new User());
                user.getUser().name = userName.getText().toString();
                MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
            }
        });
        forgetPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.instance
                        .switchScreen(ViewFlyweight.FORGET_PASSWORD);
            }
        });

    }


    private void adjustUI() {

        userName = (EditText) view.findViewById(R.id.user_name);

        login = (RelativeLayout) view.findViewById(R.id.button_login);

        password = (EditText) view.findViewById(R.id.password);

        forgetPassword = (TextView) view.findViewById(R.id.forget_password);
        adjustSmallFontSize(forgetPassword);

    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX,
                        float velocityY) {

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.instance);

        builder.setTitle("退出提示")
                .setMessage("你是要退出东瓜电台吗？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                MainActivity.instance.finish();
                                System.exit(0);
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    @Override
    public void onTouch(MotionEvent event) {
        MainActivity.instance.hideKeyboard();
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


    public class LoginCallback implements Http.Callback {

        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, "Http.Callback");
            Log.e(TAG, jsonObject.toString());

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        JSONObject data = (JSONObject) jsonObject.get("data");
                        User user = new User(data);
                        UserManager mgr = UserManager.getInstance();
                        mgr.setUser(user);

                        mHandler.sendEmptyMessage(CLEAR_TEXT);

                        // TODO check voice api
                        LoginInfo userInfo = new LoginInfo();
                        userInfo.setUserId(user._id);
                        userInfo.setPassword("");
                        MainActivity.instance.voiceApi.setLoginInfo(userInfo);


                        MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);

                    } else {
                        String message = (String) state.get("msg");
                        MainActivity.instance.showMessage(message);
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


    Handler mHandler = new Handler() {

        public void handleMessage(Message message) {
            userName.setText("");
            password.setText("");
        }
    };


    private RelativeLayout login;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(MainActivity.instance, R.string.userid_found,
                        Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {
                Log.e(TAG, "MSG_LOGIN");
                Platform plat = (Platform) msg.obj;

            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(MainActivity.instance, R.string.auth_cancel,
                        Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(MainActivity.instance, R.string.auth_error,
                        Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(MainActivity.instance, R.string.auth_complete,
                        Toast.LENGTH_SHORT).show();
                System.out.println("--------MSG_AUTH_COMPLETE-------");

            }
            break;
        }
        return false;
    }

}
