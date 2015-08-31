package com.ddicar.melonradio.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.RadioText;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.service.FavoriteService;
import com.ddicar.melonradio.service.NetWorkStatus;
import com.ddicar.melonradio.service.RadioTextManager;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.web.Http;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PlayView extends AbstractView implements OnInitListener,
		PlatformActionListener, OnUtteranceCompletedListener {

	private static final String TAG = "Play";
	private static final int HIDE_ICON = 0x0039;
	private static final int SHOW_ICON = 0x0059;
	protected static final int SHOW_COVER = 0x0069;
	protected static final int HIDE_COVER = 0x0079;
	protected static final int RELOAD_PIC = 0x0089;
	protected static final int SET_TEXT = 0x0099;
	private TextToSpeech textToSpeech;
	private float playX;
	private float playY;
	private ImageView play;

	private ImageView next;
	private ImageView prev;
	private ImageView favorite;
	private ImageView share;

	private static final String nextS = "next";
	private static final String prevS = "prev";
	private static final String favoriteS = "favorite";
	private static final String shareS = "share";

	private LinearLayout menu;
	private LinearLayout cancel;

	protected boolean moved;
	protected boolean isMoved;
	private float realPlayX;
	private float realPlayY;
	// int realPlayXCopy = 8888;
	// int realPlayYCopy = 8888;

	String[] texts;

	@Override
	public void onSwitchOff() {
		// try {
		// textToSpeech.stop();
		// textToSpeech.shutdown();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	protected int lastX;
	protected int lastY;
	protected boolean clicked;

	public PlayView() {
		try {
			Context context = MainActivity.instance;
			textToSpeech = new TextToSpeech(context, PlayView.this);

			if (textToSpeech != null) {
				System.out.println("set completed listener.");
				textToSpeech.setOnUtteranceCompletedListener(this);
			}
		} catch (Exception e) {
			MainActivity.instance.showMessage("初始化文字转语音引擎失败。");
			e.printStackTrace();
		}

		try {
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(MainActivity.instance));

			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.displayer(new RoundedBitmapDisplayer(500)).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void auto() {
		Log.i(TAG, "auto");

		adjustUI();
		reloadPic();
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "play");

				if (stopped) {
					if (!didOther) {
						play();
						// Toast.makeText(MainActivity.instance, "播放开始",
						// Toast.LENGTH_SHORT).show();
					}
					didOther = false;
				} else {
					if (!playing) {
						stopped = true;
						// Toast.makeText(MainActivity.instance, "播放停止",
						// Toast.LENGTH_SHORT).show();
						pause();
					}
					playing = false;
				}

				clicked = true;
				if (!moved) {
					setNormalPosition();
				}
			}
		});

		play.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					moved = false;
					clicked = false;
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();

					lastX = x;
					lastY = y;
					hideCover();

					break;
				case MotionEvent.ACTION_UP:
					if (isInside(favoriteS)) {
						doFavorite();
					} else if (isInside(shareS)) {
						doShare();
					} else if (isInside(prevS)) {
						doPrev();
					} else if (isInside(nextS)) {
						doNext();
					} else {
						didOther = false;
						playing = false;
						// Toast.makeText(MainActivity.instance, "怎么都没走？",
						// Toast.LENGTH_SHORT).show();
					}

					if (moved) {
						bounceBack();
					}
					hide();
					isMoved = false;
					break;
				case MotionEvent.ACTION_MOVE:
					moved = true;
					x = (int) event.getRawX();
					y = (int) event.getRawY();
					lastX = x;
					lastY = y;

					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;

					show();

					setPosition(dx, dy);

					lastX = x;
					lastY = y;

					isMoved = true;

					break;
				}

				return false;
			}

		});

		ImageView radios = (ImageView) view.findViewById(R.id.button_radios);

		radios.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.DAIRY);
			}
		});

		ImageView settings = (ImageView) view
				.findViewById(R.id.button_settings);

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.SETTING);
			}
		});

		ImageView chatroom = (ImageView) view
				.findViewById(R.id.button_chatroom);

		chatroom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				stopped = true;
				pause();
				MainActivity.instance.switchScreen(ViewFlyweight.CHATROOMS);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				menu.setVisibility(View.INVISIBLE);
			}
		});

		ImageView image_wechat = (ImageView) view
				.findViewById(R.id.image_wechat_cycle);
		image_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				share(WechatMoments.NAME);
			}
		});
	}

	private void show() {

		Message msg = new Message();
		msg.what = SHOW_ICON;
		mHandler.sendMessageDelayed(msg, 500);

	}

	private void hide() {

		Message msg = new Message();
		msg.what = HIDE_ICON;
		mHandler.sendMessageDelayed(msg, 500);

	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message message) {
			switch (message.what) {
			case HIDE_ICON:

				favorite.setVisibility(View.INVISIBLE);
				share.setVisibility(View.INVISIBLE);
				prev.setVisibility(View.INVISIBLE);
				next.setVisibility(View.INVISIBLE);
				break;

			case SHOW_ICON:
				favorite.setVisibility(View.VISIBLE);
				share.setVisibility(View.VISIBLE);
				prev.setVisibility(View.VISIBLE);
				next.setVisibility(View.VISIBLE);
				break;

			case SHOW_COVER:
				playCover.setVisibility(View.VISIBLE);
				outsider.setVisibility(View.VISIBLE);
				break;

			case HIDE_COVER:
				playCover.setVisibility(View.INVISIBLE);
				outsider.setVisibility(View.INVISIBLE);
				break;

			case RELOAD_PIC:
				RadioText radio = (RadioText) message.obj;
				if (imageLoader != null && radio != null) {
					imageLoader.displayImage(Http.SERVER + "images/"
							+ radio.pic, play, options);
				}
				break;

			case SET_TEXT:
				radio = (RadioText) message.obj;
				radioText.setText(radio.title.replaceAll("\r", "").replaceAll(
						"\n", ""));
				radioText.requestFocus();
				break;
			}
		}
	};
	// private boolean ttsBound;
	// private ITts ttsService;
	private ImageView playCover;
	private boolean didOther;
	boolean stopped = false;
	boolean playing = false;
	private TextView radioText;
	private int segment;
	private int countOfSegments;
	private ImageView outsider;
	private int count;

	protected void doNext() {
		Log.i(TAG, "doNext");
		didOther = true;
		playing = true;
		count++;
		stop();
		RadioTextManager manager = RadioTextManager.getInstance();
		RadioText currentRadioText = manager.next();
		if (currentRadioText != null) {
			// textToSpeech.speak(currentRadioText.content,
			// TextToSpeech.QUEUE_FLUSH, null);
			play();
			MainActivity.instance.showMessage("播放下一首");
		} else {
			MainActivity.instance.showMessage("已经是最后一首了");
		}
		// Toast.makeText(MainActivity.instance, "doNext调用了"+count+"次",
		// Toast.LENGTH_SHORT).show();
	}

	protected void doPrev() {
		Log.i(TAG, "doPrev");
		didOther = true;
		playing = true;
		stop();
		RadioTextManager manager = RadioTextManager.getInstance();
		RadioText currentRadioText = manager.prev();
		if (currentRadioText != null) {
			// textToSpeech.speak(currentRadioText.content,
			// TextToSpeech.QUEUE_FLUSH, null);
			play();
			MainActivity.instance.showMessage("播放上一首");
		} else {
			MainActivity.instance.showMessage("已经是第一首了");
		}
	}

	public void reloadPic(RadioText radio) {
		Message message = new Message();
		message.what = RELOAD_PIC;
		message.obj = radio;
		mHandler.sendMessage(message);
	}

	public void reloadPic() {

		RadioTextManager manager = RadioTextManager.getInstance();
		RadioText currentRadioText = manager.getCurrentRadioText();
		reloadPic(currentRadioText);
	}

	public void play() {

		// if (ttsBound) {
		//
		// RadioTextManager manager = RadioTextManager.getInstance();
		// RadioText currentRadioText = manager.getCurrentRadioText();
		// reloadPic(currentRadioText);
		// playCover.setVisibility(View.INVISIBLE);
		//
		// if (currentRadioText != null) {
		// speechText(currentRadioText.content);
		// }
		// }

		if (textToSpeech != null && !textToSpeech.isSpeaking()) {
			textToSpeech.setPitch(0.9f);

			RadioTextManager manager = RadioTextManager.getInstance();
			RadioText currentRadioText = manager.getCurrentRadioText();
			reloadPic(currentRadioText);

			hideCover();

			if (currentRadioText != null) {
				Log.i(TAG, currentRadioText.content);

				HashMap<String, String> params = new HashMap<String, String>();
				// Utterance_ID是String类型的
				params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"UTTERANCE_ID");

				texts = currentRadioText.content.split("。");

				countOfSegments = texts.length;
				// for (int i = segment; i < countOfSegments; i++) {
				System.out.println("speaking..  " + segment);
				textToSpeech.speak(texts[segment], TextToSpeech.QUEUE_FLUSH,
						params);

				stopped = false;
				// Toast.makeText(MainActivity.instance, "play播放开始",
				// Toast.LENGTH_SHORT).show();
				setText(currentRadioText);
			}
		} else {
			stopped = true;
			// Toast.makeText(MainActivity.instance, "play播放停止",
			// Toast.LENGTH_SHORT).show();
			Log.i(TAG, "TTS not initialized");
		}

	}

	private void setText(RadioText radio) {
		Message message = new Message();
		message.what = SET_TEXT;
		message.obj = radio;
		mHandler.sendMessage(message);
	}

	private void hideCover() {
		Message message = new Message();
		message.what = HIDE_COVER;
		mHandler.sendMessage(message);
	}

	private void showCover() {
		Message message = new Message();
		message.what = SHOW_COVER;
		mHandler.sendMessage(message);
	}

	protected void doShare() {
		Log.i(TAG, "doShare");
		didOther = true;
		// MainActivity.instance.showMessage("分享");
		menu.setVisibility(View.VISIBLE);
	}

	protected void doFavorite() {
		Log.i(TAG, "doFavorite");

		didOther = true;

		MainActivity.instance.showMessage("收藏");

		RadioTextManager radioTextManager = RadioTextManager.getInstance();
		RadioText radioText = radioTextManager.getCurrentRadioText();

		if (radioText != null) {
			FavoriteService favoriteService = FavoriteService.getInstance();
			favoriteService.favorite(radioText);
		}

	}

	// protected boolean isOverlapped(ImageView icon, String text) {
	//
	// int left = icon.getLeft();
	// int top = icon.getTop();
	// int right = icon.getRight();
	// int bottom = icon.getBottom();
	//
	// if (isInside( text)) {
	// return true;
	// }
	// // if (isInside(left, bottom, text)) {
	// // return true;
	// // }
	// // if (isInside(right, top, text)) {
	// // return true;
	// // }
	// // if (isInside(right, bottom, text)) {
	// // return true;
	// // }
	// return false;
	// }

	private boolean isInside(String text) {

		int left = (int) (realPlayX - 72);
		int top = (int) (realPlayY - 72);
		int right = (int) (realPlayX + 72);
		int bottom = (int) (realPlayY + 72);

		int[] windowSize = MainActivity.instance.getWindowSize();
		int screenWidth = windowSize[0];
		int screenHeight = windowSize[1];

		// System.out.println(left + "," + top + "-" + right + "," + bottom
		// + " === " + x + "," + y);

		if (0 != realPlayX && isMoved) {

			if (prevS.equals(text)) {

				if ((screenWidth - left) > left
						&& ((screenHeight / 2) - top) < top
						&& right <= ((screenWidth / 2) - 120)
						|| (screenWidth - left) > left
						&& (bottom - (screenHeight / 2)) < (screenHeight - bottom)
						&& right < ((screenWidth / 2) - 120)) {
					// Toast.makeText(MainActivity.instance, "向前判断通过",
					// Toast.LENGTH_SHORT).show();
					return true;
				}

			}
			if (nextS.equals(text)) {
				if ((screenWidth - right) < right
						&& ((screenHeight / 2) - top) < top
						&& left >= ((screenWidth / 2) + 60)
						|| (screenWidth - right) < right
						&& (bottom - (screenHeight / 2)) < (screenHeight - bottom)
						&& left > ((screenWidth / 2) + 60)) {
					// Toast.makeText(MainActivity.instance, "向后判断通过",
					// Toast.LENGTH_SHORT).show();
					// Toast.makeText(MainActivity.instance, "左边="+left,
					// Toast.LENGTH_SHORT).show();
					// Toast.makeText(MainActivity.instance, "高度="+screenHeight,
					// Toast.LENGTH_SHORT).show();
					return true;
				}

			}

			if (favoriteS.equals(text)) {
				if ((screenHeight / 2 - top - 200) > top
						&& right > ((screenWidth / 2) - 120)
						&& left < ((screenWidth / 2) + 120)) {
					// Toast.makeText(MainActivity.instance,
					// "高度一半="+screenHeight/2, Toast.LENGTH_SHORT).show();
					// Toast.makeText(MainActivity.instance, "top="+top,
					// Toast.LENGTH_SHORT).show();
					// Toast.makeText(MainActivity.instance, "上边="+top,
					// Toast.LENGTH_SHORT).show();
					return true;
				}

			}
			if (shareS.equals(text)) {
				if ((screenHeight - bottom) < bottom
						&& (bottom - (screenHeight / 2)) > (screenHeight - bottom)
						|| (screenHeight - bottom) < bottom
						&& right > ((screenWidth / 2) - 120)
						&& left < ((screenWidth / 2) + 120)) {
					// Toast.makeText(MainActivity.instance, "下边="+bottom,
					// Toast.LENGTH_SHORT).show();
					return true;
				}

			}

		}

		// if (left <= x && top <= y && x <= right && y <= bottom) {
		// // System.out.println("[------INSIDE------]");
		// return true;
		// }

		return false;
	}

	private void adjustUI() {

		int[] sizes = MainActivity.instance.getWindowSize();

		int marginTop = 28;

		int screenX = sizes[0];
		int screenY = sizes[1];

		favorite = (ImageView) view.findViewById(R.id.button_favorite);

		adjustWidth(favorite, 60);

		next = (ImageView) view.findViewById(R.id.button_next);
		adjustWidth(next, 60);

		prev = (ImageView) view.findViewById(R.id.button_prev);

		adjustWidth(prev, 60);

		share = (ImageView) view.findViewById(R.id.button_share);
		adjustWidth(share, 60);

		outsider = (ImageView) view.findViewById(R.id.outsider);

		playCover = (ImageView) view.findViewById(R.id.button_play_cover);

		menu = (LinearLayout) view.findViewById(R.id.menu_container);

		cancel = (LinearLayout) view.findViewById(R.id.button_cancel);

		TextView cancelMiddle = (TextView) view
				.findViewById(R.id.cancel_middle);
		adjustFullWidth(cancelMiddle);
		adjustUnitSize(cancelMiddle);
		adjustFontSize(cancelMiddle);

		// ImageView cancelLeft = (ImageView)
		// view.findViewById(R.id.cancel_left);
		// adjustUnitSize(cancelLeft, 13);
		//
		// ImageView cancelRight = (ImageView) view
		// .findViewById(R.id.cancel_right);
		// adjustUnitSize(cancelRight, 13);

		play = (ImageView) view.findViewById(R.id.button_play);

		RadioTextManager manager = RadioTextManager.getInstance();
		RadioText currentRadioText = manager.getCurrentRadioText();
		if (currentRadioText != null) {
			imageLoader.displayImage(Http.SERVER + "images/"
					+ currentRadioText.pic, play, options);
		}

		playX = screenX / 2 - AndroidUtil.pixel(125);
		playY = (screenY - marginTop) / 2 - AndroidUtil.pixel(125);

		System.out.println("playX, playY = " + playX + "," + playY);

		setNormalSize();
		play.setX(playX);
		play.setY(playY);

		radioText = (TextView) view.findViewById(R.id.radio_text);
	}

	/**
	 * 分享
	 * 
	 * @param platformName
	 *            平台名称
	 */
	private void share(String platformName) {

		// 参数
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		Bitmap imageData = BitmapFactory.decodeResource(
				MainActivity.instance.getResources(), R.drawable.dark_logo);
		Bitmap resizeBmp = small(imageData);
		sp.setImageData(resizeBmp);
		// sp.setImageUrl(mCoverBigUrl);
		sp.setTitle("东瓜日报");

		UserManager manager = UserManager.getInstance();
		User user = manager.getUser();
		RadioTextManager radiomanager = RadioTextManager.getInstance();
		RadioText currentRadioText = radiomanager.getCurrentRadioText();
		String urlEncode = "http://www.ddicar.com/Journal/Journal.html?userid="
				+ user._id + "&postid=" + currentRadioText._id;
		sp.setUrl(urlEncode);
		// sp.setTitleUrl(mShareUrl);
		String title_encode = null;
		try {
			title_encode = URLEncoder.encode(radioText.getText().toString()
					.trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sp.setText(title_encode);
		// 平台
		Platform platFriends = ShareSDK.getPlatform(
				MainActivity.instance.getApplicationContext(), platformName);
		platFriends.setPlatformActionListener(this);
		platFriends.share(sp);
		Toast.makeText(MainActivity.instance, "分享成功",
				 Toast.LENGTH_SHORT).show();
	}

	private Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.83f, 0.7f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {

	}

	@Override
	public void onComplete(Platform platform, int i,
			HashMap<String, Object> stringObjectHashMap) {
		Log.e(TAG, "wechat share ok");
		// if(platform.getName().equals(SinaWeibo.NAME)){
		// Toast.makeText(mContext, "分享成功", 0).show();
		// dismiss();
		// }

		// sendSharedNumAdd();
		// Iterator iter = stringObjectHashMap.entrySet().iterator();
		// while (iter.hasNext()) {
		// Map.Entry entry = (Map.Entry) iter.next();
		// Object key = entry.getKey();
		// Object val = entry.getValue();
		// Log.d("onComplete", "key = " + key + " --- val = " + val);
		// }
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {
		Log.i(TAG, "onFling");

	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "onBackPressed");

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
		System.out.println("Touched");

	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume");

	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause");

		// try {
		// textToSpeech.stop();
		// textToSpeech.shutdown();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}


	@Override
	public void onInit(int status) {
		Log.i(TAG, "onInit");
		if (status == TextToSpeech.SUCCESS) {
			int result = textToSpeech.setLanguage(Locale.CHINA);
			System.out.println("result = " + result);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {

				if (!AndroidUtil.isAppInstalled("com.iflytek.tts")) {
					installFlyTekTts();
				}

				// 加载科大讯飞TTS引擎

				// 加载手说TTS

				// if (!AndroidUtil.isAppInstalled("com.shoushuo.android.tts"))
				// {
				// alertInstallShouShuo();
				// }
				//
				// if (!ttsBound) {
				// System.out.println("binding shoushuo service.");
				// String actionName =
				// "com.shoushuo.android.tts.intent.action.InvokeTts";
				// Intent intent = new Intent(actionName);
				// // 绑定tts服务
				// MainActivity.instance.bindService(intent, ttsConnection,
				// Context.BIND_AUTO_CREATE);
				// }
			}
		}

	}
	
	File file = new File(Environment
            .getExternalStorageDirectory()
            + "/iflytek_tts");
	
	private void installFlyTekTts() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.instance);
		// builder.setIcon(R.drawable.logo);
		builder.setTitle("安装 文本到语音转换引擎");
		builder.setMessage("您需要下载并安装文本到语音转换引擎才能够收听广播。");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
					// String fileName = "file://android_asset/tts.apk";
//					Intent intent = new Intent(Intent.ACTION_VIEW);
//
//					AssetManager assets = MainActivity.instance.getResources()
//							.getAssets();
//
//					String[] fileName = assets.list("");
//
//					for (String fName : fileName) {
//						System.out.println("fileName = " + fName);
//						if (fName.contains("iflytek_tts.apk")) {
//
//							InputStream is = assets.open(fName);
//
//							String targetFileName = Environment
//									.getExternalStorageDirectory()
//									+ "/iflytek_tts.apk";
//
//							FileOutputStream fos = new FileOutputStream(
//									targetFileName);
//							byte[] buffer = new byte[7168];
//							int count = 0;
//							while ((count = is.read(buffer)) > 0) {
//								fos.write(buffer, 0, count);
//							}
//							fos.close();
//							is.close();
//
//							File file = new File(targetFileName);
//
//							System.out.println("file = " + file);
//
//							Uri uri = Uri.fromFile(file);
//							System.out.println("uri = " + uri);
//							intent.setDataAndType(uri,
//									"application/vnd.android.package-archive");
//							MainActivity.instance.startActivity(intent);
//
//						}
//					}
					
//					new Thread() {
//	                    public void run() {
	                        
//	                        String path = "http://apkfile.shouji.com.cn/kf/soft/20120719/4710178845.apk?filename=tts_service_1.0.apk";
//	                      String path = "http://192.168.1.62:8080/MyHttpTest.apk";
	                        try {
//	                            URL url = new URL(path);
//	                            HttpURLConnection conn = (HttpURLConnection) url
//	                                    .openConnection();
	                           
	                                if (Environment.getExternalStorageState()
	                                        .equals(Environment.MEDIA_UNMOUNTED)) {
	                                	Toast.makeText(MainActivity.instance, "没有SD卡",
	                                            Toast.LENGTH_SHORT).show();
	                                } else {
	                                    // System.out.println("获取信息的长度："+conn.getContentLength());
	                                    
	                                    if (!file.exists()) {
	                                        file.mkdir();
	                                    }
	                                    
	                                    // System.out.println("file.getPath():"
	                                    // + file.getPath());
	                                    // System.out.println("getContentLength:"
	                                    // + conn.getContentLength());
//	                                    File cfile = new File(file.getPath(),
//	                                            "iflytek_tts.apk");
//	                                    if (!cfile.exists()) {
//	                                        cfile.createNewFile();
//	                                    }
	                                    String serviceString =Context.DOWNLOAD_SERVICE;
	                                    
	                                    DownloadManager downloadManager  = (DownloadManager)MainActivity.instance.getSystemService(serviceString);
	                                    
	                                    Uri uri =Uri.parse("http://apkfile.shouji.com.cn/kf/soft/20120719/4710178845.apk?filename=tts_service_1.0.apk");
	                                    DownloadManager.Request request = new DownloadManager.Request(uri);
	                                    
	                                    NetWorkStatus netWorkStatus = NetWorkStatus.getInstance();
	                                    netWorkStatus.isWifiNetwork();
	                                   
	                                    request.setDestinationInExternalPublicDir("iflytek_tts", "iflytek_tts.apk");
	                                    // 设置下载后文件存放的位置--如果目标位置已经存在这个文件名，则不执行下载，所以用date 类型随机取名。
	                                    //request.setDestinationInExternalFilesDir(MainActivity.instance, null,"iflytek_tts.apk");
	                                    //Toast.makeText(MainActivity.instance, file.getPath(), Toast.LENGTH_SHORT).show();
	                                    request.setTitle("讯飞语音");
	                                    request.setDescription("正在下载");
	                                    request.setAllowedOverRoaming(false);
	                                    downloadManager.enqueue(request);
	                                    
	                                    pauseReason(downloadManager);
	                                    
	                                    
	                                    
	                                 // 监听下载结束，启用BroadcastReceiver  
	                                    BroadcastReceiver receiver = new BroadcastReceiver() { 
	                                	    @Override
	                                	    public void onReceive(Context context, Intent intent) {
	                                	        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
	                                	            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
	                                	            //Toast.makeText(MainActivity.instance, "讯飞语音的下载任务已经完成！", Toast.LENGTH_SHORT).show();
	                                	            new  AlertDialog.Builder(MainActivity.instance)    
	                                	    		.setTitle("讯飞语音的下载安装已经完成" )  
	                                	    		.setMessage("安装完成后，需要您到输入法设置里面找到\"讯飞语音合成的设置\"->\"文字转语音(TTS)输出\",选择\"讯飞语音合成\"选项" )  
	                                	    		.setPositiveButton("确定" ,  null )
	                                	    		.show();  
	                                	            /**
	                                                 * The download manager is a system service that handles long-running HTTP downloads.
	                                                 */
	                                                DownloadManager downloadManager = (DownloadManager) MainActivity.instance
	                                                        .getSystemService(MainActivity.instance.DOWNLOAD_SERVICE);//从下载服务获取下载管理器
	                                                String fileName = "";
	                                                Query query = new Query();
	                                 
	                                                query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);//设置过滤状态：成功
	                                                Cursor c = downloadManager.query(query);// 查询以前下载过的‘成功文件’
	                                                
	                                                if (c.moveToFirst()) {// 移动到最新下载的文件
	                                                    fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
	                                                }
	                                                //Toast.makeText(MainActivity.instance, "文件名="+fileName, Toast.LENGTH_SHORT).show();
	                                                System.out.println("======文件名称=====" + fileName);
	                                                //Toast.makeText(MainActivity.instance, file.getPath()+"/iflytek_tts.apk", Toast.LENGTH_LONG).show();
	                                                
	                                                installApk(fileName.replace("file://", ""));
	                                               
	                                	        }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
	                                	            Toast.makeText(MainActivity.instance, "您的任务正在下载中", Toast.LENGTH_SHORT).show();
	                                	        }
	                                	    }
	                                    };
	                                    MainActivity.instance.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	                                    
//	                                    InputStream is = conn.getInputStream();
//	                                    FileOutputStream os = new FileOutputStream(
//	                                            cfile);
//	                                    byte[] buffer = new byte[2048];
//	                                    conn.connect(); 
//	                                    int a=0;
//	                                    while ((a=is.read(buffer)) != -1) {
//	                                        os.write(buffer, 0, a);
//	                                    }
////	                                  System.out.println("cfile.getName()"+cfile.getName());
//	                                    conn.disconnect(); 
//	                                    is.close();
//	                                    os.flush();
//	                                    os.close();
	                                    
	                                }
	                            
	                        } catch (Exception e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                        }

//	                    };
//	                }.start();				  
					
				
			}
			
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.show();
//		if(installFlag == 1){
//		new  AlertDialog.Builder(MainActivity.instance)    
//		.setTitle("讯飞语音的下载任务已经完成" )  
//		.setMessage("安装完成后，需要您到输入法设置里面找到\"讯飞语音合成的设置\"->\"文字转语音(TTS)输出\",选择\"讯飞语音合成\"选项" )  
//		.setPositiveButton("确定" ,  null )  
//		.show();  
//		}
	}
	
	
	
	private void pauseReason(DownloadManager downloadManager) {
		// Create a query for pauseddownloads.

		Query pausedDownloadQuery = new Query();

		pausedDownloadQuery.setFilterByStatus(DownloadManager.STATUS_PAUSED);

		// Query the Download Manager for pauseddownloads.

		Cursor pausedDownloads = downloadManager.query(pausedDownloadQuery);

		// Find the column indexes for the data werequire.

		int reasonIdx = pausedDownloads
				.getColumnIndex(DownloadManager.COLUMN_REASON);

		// Iterate over the result Cursor.

		while (pausedDownloads.moveToNext()) {

			// Translate the pause reason to friendly text.

			int reason = pausedDownloads.getInt(reasonIdx);

			switch (reason) {

			case DownloadManager.ERROR_DEVICE_NOT_FOUND:

				Toast.makeText(MainActivity.instance, "存储空间已满",
						Toast.LENGTH_SHORT).show();
				break;

			case DownloadManager.PAUSED_WAITING_TO_RETRY:

				Toast.makeText(MainActivity.instance, "等待重新连接网络",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;

			}
			// Close the result Cursor.

			pausedDownloads.close();
		}
	}

	//安装apk文件
	 private void installApk(String filename)
	 {
	  File file = new File(filename);
	  Intent intent = new Intent();
	  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  intent.setAction(Intent.ACTION_VIEW);     //浏览网页的Action(动作)
	  String type = "application/vnd.android.package-archive";
	  intent.setDataAndType(Uri.fromFile(file), type);  //设置数据类型
	  MainActivity.instance.startActivity(intent);
	 }
	
	private void alertInstallShouShuo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.instance);
		// builder.setIcon(R.drawable.logo);
		builder.setTitle("安装 文本到语音转换引擎");
		builder.setMessage("您需要安装文本到语音转换引擎才能够收听广播。");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					// String fileName = "file://android_asset/tts.apk";
					Intent intent = new Intent(Intent.ACTION_VIEW);

					AssetManager assets = MainActivity.instance.getResources()
							.getAssets();

					String[] fileName = assets.list("");

					for (String fName : fileName) {
						System.out.println("fileName = " + fName);
						if (fName.contains("tts.apk")) {

							InputStream is = assets.open(fName);

							String targetFileName = Environment
									.getExternalStorageDirectory() + "/tts.apk";

							FileOutputStream fos = new FileOutputStream(
									targetFileName);
							byte[] buffer = new byte[7168];
							int count = 0;
							while ((count = is.read(buffer)) > 0) {
								fos.write(buffer, 0, count);
							}
							fos.close();
							is.close();

							File file = new File(targetFileName);

							System.out.println("file = " + file);

							Uri uri = Uri.fromFile(file);
							System.out.println("uri = " + uri);
							intent.setDataAndType(uri,
									"application/vnd.android.package-archive");
							MainActivity.instance.startActivity(intent);

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.show();

	}

	// /**
	// * 回调参数.
	// */
	// private final ITtsCallback ttsCallback = new ITtsCallback.Stub() {
	// // 朗读完毕.
	// @Override
	// public void speakCompleted() throws RemoteException {
	// Message msg = new Message();
	// msg.what = SHOW_COVER;
	// mHandler.sendMessage(msg);
	// }
	// };

	// public void speechText(String text) {
	// try {
	// ttsService.speak(text, TextToSpeech.QUEUE_FLUSH);
	// } catch (RemoteException e) {
	// e.printStackTrace();
	// }
	// }

	// /**
	// * tts服务连接.
	// */
	// private final ServiceConnection ttsConnection = new ServiceConnection() {
	// @Override
	// public void onServiceDisconnected(ComponentName arg0) {
	// try {
	// // 撤销回调参数.
	// ttsService.unregisterCallback(ttsCallback);
	// } catch (RemoteException e) {
	// e.printStackTrace();
	// }
	// ttsService = null;
	// ttsBound = false;
	// }
	//
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// ttsService = ITts.Stub.asInterface(service);
	// ttsBound = true;
	// try {
	// // tts服务初始化
	// ttsService.initialize();
	// // 注册回调参数
	// ttsService.registerCallback(ttsCallback);
	//
	// System.out.println("service connected");
	//
	// } catch (RemoteException e) {
	// e.printStackTrace();
	// }
	// }
	// };

	private void setPosition(int x, int y) {

		realPlayX = lastX + x - AndroidUtil.pixel(62);
		realPlayY = lastY + y - AndroidUtil.pixel(62);

		LayoutParams params = (LayoutParams) play.getLayoutParams();
		params.width = AndroidUtil.pixel(125);
		params.height = AndroidUtil.pixel(125);
		play.setLayoutParams(params);

		play.setX(realPlayX);
		play.setY(realPlayY - 28);

	}

	private void setNormalPosition() {

		setNormalSize();

		// if (clicked) {
		// System.out.println("clicked");
		play.setX(playX);
		play.setY(playY);
		// } else {
		// System.out.println("not - clicked");
		// play.setX(playX + AndroidUtil.pixel(62));
		// play.setY(playY + AndroidUtil.pixel(62));
		// }
	}

	private void setNormalSize() {
		LayoutParams params = (LayoutParams) play.getLayoutParams();

		params.width = AndroidUtil.pixel(250);
		params.height = AndroidUtil.pixel(250);
		play.setLayoutParams(params);
	}

	protected void bounceBack() {

		final float dx = playX + AndroidUtil.pixel(62) - realPlayX;
		final float dy = playY + AndroidUtil.pixel(62) - realPlayY;

		ValueAnimator valueAnimator = new ValueAnimator();

		// final ValueAnimator bounceAnimator = new ValueAnimator();

		valueAnimator.setDuration(400);
		valueAnimator.setObjectValues(new PointF(0, 0));
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {

			@Override
			public PointF evaluate(float fraction, PointF startValue,
					PointF endValue) {

				PointF point = new PointF();
				// point.x = dx * 1.5f * fraction;
				// point.y = dy * 1.5f * fraction;
				// point.x = -(Math.abs(60 - fraction * 100)/60 *dx) + 40/60 *
				// dx;
				// point.y = -(Math.abs(60 - fraction * 100)/60 *dy) + 40/60 *
				// dx;

				point.x = (-Math.abs(80 - fraction * 100) + 120) / 100 * 1.2f
						* dx;
				point.y = (-Math.abs(80 - fraction * 100) + 120) / 100 * 1.2f
						* dy;

				return point;
			}
		});

		valueAnimator.start();
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {

				PointF point = (PointF) animation.getAnimatedValue();
				System.out.println(point);
				play.setX(realPlayX + point.x);
				play.setY(realPlayY + point.y);
			}

		});

		valueAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				Log.e(TAG, "end");
				setNormalPosition();
				// bounceAnimator.start();
			}
		});

		System.out.println("-----------");
		//
		// bounceAnimator.setDuration(300);
		// bounceAnimator.setObjectValues(new PointF(dx, dy));
		// bounceAnimator.setInterpolator(new LinearInterpolator());
		// bounceAnimator.setEvaluator(new TypeEvaluator<PointF>() {
		//
		// @Override
		// public PointF evaluate(float fraction, PointF startValue,
		// PointF endValue) {
		//
		// PointF point = new PointF();
		// point.x = dx * 0.1f * (1 + fraction);
		// point.y = dy * 0.1f * (1 + fraction);
		// return point;
		// }
		// });
		//
		// bounceAnimator.addUpdateListener(new AnimatorUpdateListener() {
		//
		// @Override
		// public void onAnimationUpdate(ValueAnimator animation) {
		//
		// PointF point = (PointF) animation.getAnimatedValue();
		// System.out.println(point);
		// play.setX(realPlayX + point.x);
		// play.setY(realPlayY + point.y);
		// }
		//
		// });
		//
		// bounceAnimator.addListener(new AnimatorListenerAdapter() {
		// @Override
		// public void onAnimationEnd(Animator animation) {
		// Log.e(TAG, "end");
		// setNormalPosition();
		// }
		// });

	}

	public void stop() {
		try {
			showCover();
			textToSpeech.stop();
			segment = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// ttsService.stop();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	private void pause() {

		try {
			showCover();
			textToSpeech.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUtteranceCompleted(String utteranceId) {
		System.out.println("Playing end.");
		if (!stopped) {
			if (segment == countOfSegments - 1) {
				// Toast.makeText(MainActivity.instance, "最后的方法，读完",
				// Toast.LENGTH_SHORT).show();
				doNext();
			} else {
				segment++;
				System.out.println("speaking..." + segment);

				HashMap<String, String> params = new HashMap<String, String>();
				// Utterance_ID是String类型的
				params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"UTTERANCE_ID");

				textToSpeech.speak(texts[segment], TextToSpeech.QUEUE_FLUSH,
						params);
			}
		}
	}
}
