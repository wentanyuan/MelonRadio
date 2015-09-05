package com.ddicar.melonradio.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.CaptchaService;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

public class ForgetPasswordView extends AbstractView {

    private static final int CLEAR_TEXT = 0x0f;

    private static final String TAG = "ForgetPasswordView";
    private EditText phoneNumber;
    private EditText captchaCode;
//    private EditText password;

    private String phoneNumberText;
//    private String passwordText;

    protected boolean gainCaptchaClickable = true;

    private static final int UPDATE_TIMER = 0x9999;

    private TextView back;

    private RelativeLayout gainCaptcha;

    private TextView gainCaptchaMiddle;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {

        adjustUI();

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.instance.switchScreen(ViewFlyweight.LOGIN);
            }
        });

        gainCaptcha = (RelativeLayout) view.findViewById(R.id.button_gain_captcha);
        gainCaptcha.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!gainCaptchaClickable) {
                    return;
                }

                phoneNumberText = phoneNumber.getText().toString();

                if (StringUtil.isNullOrEmpty(phoneNumberText)) {
                    MainActivity.instance.showMessage("请输入手机号码");
                    phoneNumber.requestFocus();
                    return;
                }

                startCountingDown();

                CaptchaService service = CaptchaService.getInstance();

                // TODO 手机号码格式校验

                service.getCaptcha(phoneNumberText);
            }
        });

        RelativeLayout resetPassword = (RelativeLayout) view
                .findViewById(R.id.button_reset_password);

        resetPassword.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                MainActivity.instance.hideKeyboard();
                phoneNumberText = phoneNumber.getText().toString();
                String captchaText = captchaCode.getText().toString();

                if (StringUtil.isNullOrEmpty(phoneNumberText)) {
                    MainActivity.instance.showMessage("请输入手机号");
                    return;
                }

                if (StringUtil.isNullOrEmpty(captchaText)) {
                    MainActivity.instance.showMessage("请输入验证码");
                    return;
                }

                CaptchaService service = CaptchaService.getInstance();
                service.submitCaptcha(phoneNumberText, captchaText,
                        new ForgetPasswordCallback());

            }
        });
    }

    private void adjustUI() {
        back = (TextView) view.findViewById(R.id.button_back);

        phoneNumber = (EditText) view.findViewById(R.id.phone_number);

        captchaCode = (EditText) view.findViewById(R.id.captcha_code);

        gainCaptchaMiddle = (TextView) view.findViewById(R.id.gain_captcha);

    }


    private void startCountingDown() {

        Timer timer = new Timer(true);

        task = new MyTimerTask();
        timer.schedule(task, 0, 1000);
    }

    TimerTask task;

    class MyTimerTask extends TimerTask {

        int count = 0;

        @Override
        public void run() {
            Message message = new Message();
            message.what = UPDATE_TIMER;
            message.arg1 = count;
            mHandler.sendMessage(message);
            count++;
        }

    }


    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX,
                        float velocityY) {

    }

    @Override
    public void onBackPressed() {
        MainActivity.instance.switchScreen(ViewFlyweight.LOGIN);

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




    Handler mHandler = new Handler() {

        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_TIMER:

                    int count = message.arg1;
                    int number = 60 - count;

                    gainCaptchaMiddle.setText("获取验证码(" + number + ")");

                    if (number > 0) {
                        gainCaptchaMiddle.setTextColor(Color.GRAY);
                        gainCaptchaClickable = false;
                    } else {
                        gainCaptchaMiddle.setText("获取验证码");
                        gainCaptchaMiddle.setTextColor(Color.argb(0xff, 0x43, 0xCC,
                                0x8D));
                        gainCaptchaClickable = true;
                        task.cancel();
                    }
                    break;
                case CLEAR_TEXT:
                    phoneNumber.setText("");
                    captchaCode.setText("");
                    break;
            }
        }
    };


    private class ForgetPasswordCallback implements CaptchaService.Callback {
        @Override
        public void run() {
            UserManager userManager = UserManager.getInstance();
            User user = new User();
            user.phone = phoneNumber.getText().toString();
            userManager.setUser(user);
            MainActivity.instance.switchScreen(ViewFlyweight.RESET_PASSWORD);
        }
    }

}
