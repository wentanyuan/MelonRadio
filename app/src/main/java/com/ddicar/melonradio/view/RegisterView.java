package com.ddicar.melonradio.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.CaptchaService;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.WebException;

public class RegisterView extends AbstractView implements Http.Callback,
		CaptchaService.Callback {

	private static final int CLEAR_TEXT = 0x0f;

//	private ImageView readTerm;
	private ImageView catOffLine;
	private ImageView catOffLineSecond;
	private boolean checkedTerm = true;
	private EditText phoneNumber;
	private EditText captchaCode;
	private EditText password;
	private EditText name;
//	private EditText passwordAgain;
	private LinearLayout containerCode;
	private LinearLayout firstCode;
	private LinearLayout thirdCode;
	private LinearLayout fourthCode;
	private LinearLayout secondCode;

	private String phoneNumberText;
	private String passwordText;
	private String nameText;

	protected boolean gainCaptchaClickable = true;

	private static final int UPDATE_TIMER = 0x9999;

	private static final String TAG = "register";

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {

		adjustUI();
//		readTerm.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (checkedTerm) {
//					readTerm.setImageResource(R.drawable.unchecked_checkbox);
//					checkedTerm = false;
//				} else {
//					readTerm.setImageResource(R.drawable.checked_checkbox);
//					checkedTerm = true;
//				}
//			}
//		});

		gainCaptcha = (LinearLayout) view.findViewById(R.id.button_gain_captha);
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

				
				
				//Toast.makeText(MainActivity.instance, "@@@@@@@@@"+phoneNumberText, Toast.LENGTH_LONG).show();

				

				// TODO 手机号码格式校验

				service.getCaptcha(phoneNumberText);
			}

		});
		
		

		LinearLayout register = (LinearLayout) view
				.findViewById(R.id.button_creat_account);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO avoid double click

				MainActivity.instance.hideKeyboard();

				phoneNumberText = phoneNumber.getText().toString();
				String captchaText = captchaCode.getText().toString();
				passwordText = password.getText().toString();
				nameText = name.getText().toString();
//				String passwordAgainText = passwordAgain.getText().toString();

				if (StringUtil.isNullOrEmpty(phoneNumberText)) {
					MainActivity.instance.showMessage("请输入手机号");
					return;
				}

				if (StringUtil.isNullOrEmpty(captchaText)) {
					MainActivity.instance.showMessage("请输入验证码");
					return;
				}
				if (StringUtil.isNullOrEmpty(passwordText)) {
					MainActivity.instance.showMessage("请输入密码");
					return;
				}
				if (StringUtil.isNullOrEmpty(nameText)) {
					MainActivity.instance.showMessage("请输入您的姓名");
					return;
				}
//				if (StringUtil.isNullOrEmpty(passwordAgainText)) {
//					MainActivity.instance.showMessage("请输入确认密码");
//					return;
//				}

				// TODO 6-20位字符校验

//				if (!passwordText.equals(passwordAgainText)) {
//					MainActivity.instance.showMessage("密码必须和确认密码一致");
//					return;
//				}

				if (!checkedTerm) {
					MainActivity.instance.showMessage("必须同意协议才能使用系统");
					return;
				}

				CaptchaService service = CaptchaService.getInstance();
				service.submitCaptcha(phoneNumberText, captchaText,
						RegisterView.this);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.LOGIN);
			}
		});

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
			System.out.println("task running.." + count);
			count++;
		}

	};

	private void adjustUI() {
		TextView registerTitle = (TextView) view
				.findViewById(R.id.register_title);
		adjustTitleBarUnitSize(registerTitle);
		adjustFontSize(registerTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		phoneNumber = (EditText) view.findViewById(R.id.phone_number);
		//adjustFullWidth(phoneNumber);
//		adjustUnitSize(phoneNumber);
		adjustUnitHeightSize(phoneNumber);
		adjustFontSize(phoneNumber,18);
		
		phoneNumber.addTextChangedListener(new TextWatcher() {  
	        
	        @Override    
	        public void afterTextChanged(Editable s) {     
	            // TODO Auto-generated method stub     
	            Log.d("TAG","afterTextChanged--------------->");   
	        }   
	          
	        @Override 
	        public void beforeTextChanged(CharSequence s, int start, int count,  
	                int after) {  
	            // TODO Auto-generated method stub  
	            Log.d("TAG","beforeTextChanged--------------->");  
	        }  
	 
	         @Override    
	        public void onTextChanged(CharSequence s, int start, int before,     
	                int count) {     
	            Log.d("TAG","onTextChanged--------------->");    
	            phoneNumberText = phoneNumber.getText().toString();
	            Resources resources = MainActivity.instance.getResources(); 
	            Drawable btnBeforeDrawable = resources.getDrawable(R.drawable.captcha_before_border_round_rect); 
	            Drawable btnAfterDrawable = resources.getDrawable(R.drawable.captcha_after_border_round_rect); 
	            if (StringUtil.isNullOrEmpty(phoneNumberText)) {
	            	gainCaptchaMiddle.setBackground(btnBeforeDrawable);          	
				}else{
					gainCaptchaMiddle.setBackground(btnAfterDrawable);
				}
	             
	                              
	        }                    
	    });
		
		catOffLine = (ImageView) view.findViewById(R.id.cut_off_line);
		adjustFullWidth(catOffLine, 0);
		
		catOffLineSecond = (ImageView) view.findViewById(R.id.cut_off_line_second);
		adjustFullWidth(catOffLineSecond, 0);
		
//		Toast.makeText(this, "间距宽度 = " + String.valueOf(phoneNumber.getLayoutParams().width) , Toast.LENGTH_LONG).show();
//		ImageView phoneNumberLeft = (ImageView) view
//				.findViewById(R.id.phone_number_left);
//		adjustUnitSize(phoneNumberLeft, 13);
//
//		ImageView phoneNumberRight = (ImageView) view
//				.findViewById(R.id.phone_number_right);
//		adjustUnitSize(phoneNumberRight, 13);
		
		firstCode = (LinearLayout) view
				.findViewById(R.id.first_code);
		adjustRegisterSize(firstCode);
		
		thirdCode = (LinearLayout) view
				.findViewById(R.id.third_code);
		adjustRegisterSize(thirdCode);
		
		fourthCode = (LinearLayout) view
				.findViewById(R.id.fourth_code);
		
		adjustRegisterSize(fourthCode);
		
		secondCode = (LinearLayout) view
		.findViewById(R.id.second_code);
       adjustRegisterSize(secondCode);
		
		containerCode = (LinearLayout) view
				.findViewById(R.id.container_code);
		
		adjustFullWidth(containerCode, 0);

		captchaCode = (EditText) view.findViewById(R.id.captcha_code);
		adjustFullWidth(captchaCode, 400);
//		adjustUnitSize(captchaCode);
		adjustUnitHeightSize(captchaCode);
		adjustFontSize(captchaCode,18);

		LinearLayout captchaContainer = (LinearLayout) view
				.findViewById(R.id.captcha_container);
		adjustMargin(captchaContainer, 0, 0, 40, 0);

//		ImageView captchaCodeLeft = (ImageView) view
//				.findViewById(R.id.captcha_code_left);
//		adjustUnitSize(captchaCodeLeft, 13);
//
//		ImageView captchaCodeRight = (ImageView) view
//				.findViewById(R.id.captcha_code_right);
//		adjustUnitSize(captchaCodeRight, 13);

		gainCaptchaMiddle = (TextView) view
				.findViewById(R.id.gain_captcha_middle);
//		adjustUnitSize(gainCaptchaMiddle);
		adjustUnitHeightSize(gainCaptchaMiddle);
		adjustMediumFontSize(gainCaptchaMiddle);
		adjustFontSize(gainCaptchaMiddle,18);
		//adjustFixedWidth(gainCaptchaMiddle, 390);

//		ImageView gainCaptchaLeft = (ImageView) view
//				.findViewById(R.id.gain_captcha_left);
//		adjustUnitSize(gainCaptchaLeft, 13);
//
//		ImageView gainCaptchaRight = (ImageView) view
//				.findViewById(R.id.gain_captcha_right);
//		adjustUnitSize(gainCaptchaRight, 13);

		password = (EditText) view.findViewById(R.id.password);
//		adjustFullWidth(password);
//		adjustUnitHeightSize(password);
//		adjustFontSize(password,18);
		adjustUnitHeightSize(password);
		adjustFontSize(password,18);
		
		name = (EditText) view.findViewById(R.id.name);
		adjustUnitHeightSize(name);
		adjustFontSize(name,18);
		
		
//		ImageView passwordLeft = (ImageView) view
//				.findViewById(R.id.password_left);
//		adjustUnitSize(passwordLeft, 13);
//
//		ImageView passwordRight = (ImageView) view
//				.findViewById(R.id.password_right);
//		adjustUnitSize(passwordRight, 13);

//		passwordAgain = (EditText) view.findViewById(R.id.password_again);
//		adjustFullWidth(passwordAgain);
//		adjustUnitHeightSize(passwordAgain);
//		adjustFontSize(passwordAgain,18);

//		ImageView passwordAgainLeft = (ImageView) view
//				.findViewById(R.id.password_again_left);
//		adjustUnitSize(passwordAgainLeft, 13);
//
//		ImageView passwordAgainRight = (ImageView) view
//				.findViewById(R.id.password_again_right);
//		adjustUnitSize(passwordAgainRight, 13);

		TextView registerMiddle = (TextView) view
				.findViewById(R.id.register_middle);
		adjustFullWidth(registerMiddle);
//		adjustUnitSize(registerMiddle);
		adjustUnitSize(registerMiddle);
		adjustFontSize(registerMiddle);

//		ImageView registerLeft = (ImageView) view
//				.findViewById(R.id.register_left);
//		adjustUnitSize(registerLeft, 13);
//
//		ImageView registerRight = (ImageView) view
//				.findViewById(R.id.register_right);
//		adjustUnitSize(registerRight, 13);

//		TextView passwordHint = (TextView) view
//				.findViewById(R.id.password_hint);
//		adjustSmallFontSize(passwordHint);
		
		

//		TextView passwordAgainHint = (TextView) view
//				.findViewById(R.id.password_again_hint);
//		adjustSmallFontSize(passwordAgainHint);

		TextView termText = (TextView) view.findViewById(R.id.term_text);
		adjustSmallFontSize(termText);
		
		TextView serviceProtocol = (TextView) view.findViewById(R.id.service_protocol);
		adjustSmallFontSize(serviceProtocol);

//		readTerm = (ImageView) view.findViewById(R.id.read_term);
//		adjustWidth(readTerm, 20);
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}

	@Override
	public void onResponse(JSONObject jsonObject) {
		Log.i(TAG, "onResponse");
		Log.i(TAG, jsonObject.toString());

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

					MainActivity.instance.showMessage("注册成功，您的账号是"
							+ phoneNumberText);

					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.LOGIN);
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

	@Override
	public void run() {

		Log.i(TAG, "run");
		Http http = Http.getInstance();

		http.setCallback(RegisterView.this);

		String url = "users/reg";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone", phoneNumberText);
		params.put("password", passwordText);

		http.post(Http.SERVER + url, params);

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
				password.setText("");
//				passwordAgain.setText("");
				break;
			}
		}
	};

	private ImageView back;

	private LinearLayout gainCaptcha;

	private TextView gainCaptchaMiddle;

}
