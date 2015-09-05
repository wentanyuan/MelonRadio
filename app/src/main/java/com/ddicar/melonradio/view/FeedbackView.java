package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FeedbackView extends AbstractView {

    private RelativeLayout back;
    private EditText feedbackText;

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


        feedbackText = (EditText)view.findViewById(R.id.feedback_text);

        RelativeLayout confirm = (RelativeLayout)view.findViewById(R.id.button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Http http = Http.getInstance();

                http.setCallback(new SubmitCallback());

                String url = "users/feedback";

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("content", feedbackText.getText().toString());

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

    private class SubmitCallback implements Http.Callback {

        private final static String TAG = "SubmitCallback";

        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.e(TAG, jsonObject.toString());

            feedbackText.setText("");

            JSONObject state;
            try {
                state = (JSONObject) jsonObject.get("state");
                if (state != null) {
                    Boolean success = (Boolean) state.get("success");
                    if (success) {
                        MainActivity.instance.showMessage("提交反馈成功");
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
