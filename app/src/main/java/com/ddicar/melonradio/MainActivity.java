package com.ddicar.melonradio;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.ddicar.melonradio.service.CaptchaService;
import com.ddicar.melonradio.service.NetWorkStatus;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.service.VoiceListener;
import com.ddicar.melonradio.view.AbstractView;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.gotye.api.voichannel.VoiChannelAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MainActivity extends Activity implements OnGestureListener,
		OnTouchListener, IWXAPIEventHandler {

	private static final String TAG = "MainActivity";

	public static MainActivity instance;

	// public string for taking a photo
	public static String mPhotoPath;

	public static final int MESSAGE = 0xf000;

	public static final int SWITCH_SCREEN = 0x0f00;

	protected static final int CAPTCHA_CODE = 0x00f0;

	private static final int PHOTO_CLIP = 0x00f1;
	public static final int PHOTO_CAMERA = 0x00f2;
	public static final int PHOTO_ALBUM = 0x00f3;

	private AbstractView currentView;

	private GestureDetector mGestureDetector;

	public VoiChannelAPI voiceApi;
	private VoiceListener voiceListener;

	public IWXAPI weChatApi;
	private static final String VOICE_CHANNEL_APP_ID = "bcd28d42-eeb7-455f-b030-aa3805510f39";
	public static final String WE_CHAT_APP_ID = "wxfac56e93388fd4a0";
	public static final String WE_CHAT_SECRET = "c9c1a7662202fcd9183fa5774ac1eaa6";

	public boolean weChatLogin;

	private NetWorkStatus myReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		instance = this;

		SMSSDK.initSDK(this, CaptchaService.APP_KEY, CaptchaService.SCERET_CODE);
		SMSSDK.registerEventHandler(smsEventHandler);

		mGestureDetector = new GestureDetector(this, this);
		ViewFlyweight.init();
//		 switchScreen(ViewFlyweight.MAP);
		switchScreen(ViewFlyweight.HOME);

		voiceApi = VoiChannelAPI.getInstance();
		voiceApi.init(this, VOICE_CHANNEL_APP_ID);

		voiceListener = new VoiceListener();
		voiceApi.addListener(voiceListener);

		weChatApi = WXAPIFactory.createWXAPI(this, WE_CHAT_APP_ID, true);
		weChatApi.registerApp(WE_CHAT_APP_ID);
		weChatApi.handleIntent(getIntent(), MainActivity.this);
		
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver=new NetWorkStatus();
        registerReceiver(myReceiver, filter);

	}

	@Override
	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);

		System.out.println("onNewIntent...");

		setIntent(intent);
		weChatApi.handleIntent(intent, MainActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// switchScreen(ViewFlyweight.SETTING);
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();

		UserManager manager = UserManager.getInstance();
		manager.logout();


		voiceApi.removeListener(voiceListener);
		voiceApi.exit();
		weChatApi.unregisterApp();
		
		unregisterReceiver(myReceiver);
		
		super.onDestroy();
	}

	public void switchScreen(AbstractView view) {
		Log.e("switchScreen", view.getClass().toString());

		hideKeyboard();
		if (currentView != null) {
			currentView.onSwitchOff();
		}

		currentView = view;
		currentView.auto();
		setContentView(view.view);
		view.view.setOnTouchListener(this);
	}

	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(currentView.view.getWindowToken(), 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.i(TAG, "onTouch");
		mGestureDetector.onTouchEvent(event);
		currentView.onTouch(event);
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		Log.i(TAG, "onDown");
		return false;
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "onBackPressed");
		currentView.onBackPressed();
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {
		Log.i(TAG, "onFling");

		currentView.onFling(start, end, velocityX, velocityY);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent start, MotionEvent end,
			float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent event) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		return false;
	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		if (currentView != null) {
			currentView.onPause();
		}
	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		if (currentView != null) {
			currentView.onResume();
		}

		if (weChatLogin) {
			// TODO add check wechat login function
			// WeChatService.getInstance().login();
			// weChatLogin = false;
		}
	}

	public int[] getWindowSize() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		int screenHeight = dm.heightPixels;

		int densityDpi = dm.densityDpi;

		return new int[] { screenWidth, screenHeight, densityDpi };
	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message message) {

			System.out.println("what = " + message.what);
			System.out.println("arg1 = " + message.arg1);
			System.out.println("arg2 = " + message.arg2);
			System.out.println("obj = " + message.obj);

			switch (message.what) {

			case MainActivity.MESSAGE:

				if (MainActivity.instance != null) {
					Toast.makeText(instance, (String) message.obj,
							Toast.LENGTH_SHORT).show();
				}
				break;

			case SWITCH_SCREEN:
				switchScreen((AbstractView) message.obj);
				break;

			case CAPTCHA_CODE:
				switch (message.arg1) {
				case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
					if (message.arg2 == SMSSDK.RESULT_COMPLETE) {
						CaptchaService service = CaptchaService.getInstance();
						service.callback();
					} else {
						Throwable msg = (Throwable) message.obj;
						String detail;
						try {
							JSONObject data = new JSONObject(msg.getMessage());
							detail = (String) data.get("detail");
							showMessage(detail);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;

				case SMSSDK.EVENT_GET_VERIFICATION_CODE:
					if (message.arg2 == SMSSDK.RESULT_COMPLETE) {
					} else {
						JSONObject msg = (JSONObject) message.obj;
						String detail;
						try {
							detail = (String) msg.get("detail");
							showMessage(detail);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				}
				break;
			}
		}
	};

	public void showMessage(String text) {
		Message msg = new Message();
		msg.what = this.MESSAGE;
		msg.obj = text;
		mHandler.sendMessage(msg);
	}

	public void switchScreenInHandler(AbstractView target) {
		Message msg = new Message();
		msg.what = this.SWITCH_SCREEN;
		msg.obj = target;
		mHandler.sendMessage(msg);
	}

	EventHandler smsEventHandler = new EventHandler() {
		public void afterEvent(int event, int result, Object data) {
			Message msg = new Message();
			msg.what = CAPTCHA_CODE;
			msg.arg1 = event;
			msg.arg2 = result;
			msg.obj = data;
			mHandler.sendMessage(msg);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("onActivityResult : " + requestCode);

		switch (requestCode) {
		// 调用相机拍照时
		case PHOTO_CAMERA:
//			if (null != mPhotoPath) {
				startPhotoZoom(Uri.fromFile(new File(mPhotoPath)));
//				mPhotoPath = null;
//			} else {
//				switchScreen(ViewFlyweight.PERSONAL);
//			}
			break;
		// 取得裁剪后的图片
		case PHOTO_CLIP:
			if (null != data) {
				ViewFlyweight.PERSONAL.setPicToView(data);
				mPhotoPath = null;
			} else {
				switchScreen(ViewFlyweight.PERSONAL);
			}
			break;
		// 如果是直接从相册获取
		case PHOTO_ALBUM:
			if (data != null) {
				Uri imageUri = data.getData();
				if (imageUri != null) {
					// UserManager manager = UserManager.getInstance();
					// TODO
					// manager.refreshPhoto(uri);
					startPhotoZoom(imageUri);
				}
			}
			break;
		}
		// switchScreen(ViewFlyweight.PERSONAL);
	}

	/**
	 * 裁剪图片方法实现
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 180);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_CLIP);
	}

	@Override
	public void onReq(BaseReq req) {
		System.out.println(req);
	}

	@Override
	public void onResp(BaseResp resp) {
		System.out.println(resp);
		// int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// result = R.string.errcode_deny;
			break;
		default:
			// result = R.string.errcode_unknown;
			break;
		}
	}
}
