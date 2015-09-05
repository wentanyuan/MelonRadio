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
import com.ddicar.melonradio.service.CaptchaService;
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
//                passwordText = password.getText().toString();

                if (StringUtil.isNullOrEmpty(phoneNumberText)) {
                    MainActivity.instance.showMessage("请输入手机号");
                    return;
                }

                if (StringUtil.isNullOrEmpty(captchaText)) {
                    MainActivity.instance.showMessage("请输入验证码");
                    return;
                }
//                if (StringUtil.isNullOrEmpty(passwordText)) {
//                    MainActivity.instance.showMessage("请输入新密码");
//                    return;
//                }

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

    // Thread thread = new Thread() {
    //
    // public void run() {
    // int count = 0;
    // while (count < 61) {
    // try {
    //
    // Message message = new Message();
    // message.what = UPDATE_TIMER;
    // message.arg1 = count;
    // mHandler.sendMessage(message);
    //
    // sleep(1000);
    // count++;
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // };

    private void startCountingDown() {
        // thread.start();

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

    ;

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }


//	@Override
//	public void run() {
//		Log.e(TAG, "call back from captcha ");
//		// Http http = Http.getInstance();
//		//
//		// http.setCallback(ForgetPasswordView.this);
//		//
//		// String url = "users/resetPassword";
//		//
//		// Map<String, Object> params = new HashMap<String, Object>();
//		// params.put("phone", phoneNumberText);
//		// params.put("newPassword", passwordText);
//		//
//		// http.post(Http.SERVER + url, params);
//
//		String url = "users/resetPassword";
//		AsyncHttpClient client = new AsyncHttpClient();
//		RequestParams params = new RequestParams();
//		params.put("newpassword", passwordText);
//		params.put("phone", phoneNumberText);
//		client.post(Http.SERVER + url, params, new JsonHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//				super.onSuccess(statusCode, headers, response);
//				Log.e(TAG, "onSuccess");
//				Log.e(TAG, response.toString());
//
//				JSONObject state;
//				try {
//					state = (JSONObject) response.get("state");
//
//					if (state != null) {
//						Boolean success = (Boolean) state.get("success");
//						if (success) {
//
//							// JSONObject data = (JSONObject)
//							// response.get("data");
//							// User user = new User(data);
//							// UserManager mgr = UserManager.getInstance();
//							// mgr.setUser(user);
//
//							mHandler.sendEmptyMessage(CLEAR_TEXT);
//
//							// MainActivity.instance
//							// .switchScreenInHandler(ViewFlyweight.LOGIN);
//
//							AlertDialog.Builder builder = new AlertDialog.Builder(
//									MainActivity.instance);
//
//							final AlertDialog dialog = builder
//									.setTitle("提示")
//									.setMessage("密码已经修改")
//									.setPositiveButton(
//											"确定",
//											new DialogInterface.OnClickListener() {
//
//												@Override
//												public void onClick(
//														DialogInterface dialog,
//														int which) {
//													dialog.dismiss();
//													MainActivity.instance
//															.switchScreenInHandler(ViewFlyweight.LOGIN);
//												}
//											}).create();
//
//							dialog.show();
//
//							TimerTask tt = new TimerTask() {
//								@Override
//								public void run() {
//									if (dialog != null) {
//										dialog.dismiss();
//										MainActivity.instance
//												.switchScreenInHandler(ViewFlyweight.LOGIN);
//									}
//								}
//							};
//							Timer timer = new Timer(true);
//							timer.schedule(tt, 2000, 2000);
//						} else {
//							String message = (String) state.get("msg");
//							MainActivity.instance.showMessage(message);
//						}
//					}
//
//				} catch (JSONException e) {
//					Log.e(TAG, e.getMessage());
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					Throwable throwable, JSONObject errorResponse) {
//				super.onFailure(statusCode, headers, throwable, errorResponse);
//				Log.e(TAG, "onFailure");
//			}
//		});
//	}

    // @Override
    // public void onResponse(JSONObject jsonObject) {
    // System.out.println(jsonObject);
    //
    // JSONObject state;
    // try {
    // state = (JSONObject) jsonObject.get("state");
    //
    // if (state != null) {
    // Boolean success = (Boolean) state.get("success");
    // if (success) {
    //
    // JSONObject data = (JSONObject) jsonObject.get("data");
    // User user = new User(data);
    // UserManager mgr = UserManager.getInstance();
    // mgr.setUser(user);
    //
    // mHandler.sendEmptyMessage(CLEAR_TEXT);
    //
    // MainActivity.instance
    // .switchScreenInHandler(ViewFlyweight.LOGIN);
    // } else {
    // String message = (String) state.get("msg");
    // MainActivity.instance.showMessage(message);
    // }
    // }
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // }

    // @Override
    // public void setWebException(WebException webException) {
    // MainActivity.instance.showMessage("访问服务器出现错误了");
    //
    // }

    Handler mHandler = new Handler() {

        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_TIMER:

                    int count = message.arg1;
                    int number = 60 - count;

                    gainCaptchaMiddle.setText("获取验证码(" + number + ")");

                    if (number > 0) {
                        gainCaptchaMiddle.setTextColor(Color.GRAY);
                        gainCaptchaMiddle.setTextSize(16);
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
//                    password.setText("");
                    break;
            }
        }
    };


    private class ForgetPasswordCallback implements CaptchaService.Callback {
        @Override
        public void run() {
            MainActivity.instance.switchScreen(ViewFlyweight.RESET_PASSWORD);
        }
    }

}
