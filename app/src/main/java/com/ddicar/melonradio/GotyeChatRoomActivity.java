package com.ddicar.melonradio;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.util.StringUtil;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.gotye.api.voichannel.ChannelInfo;
import com.gotye.api.voichannel.ErrorType;
import com.gotye.api.voichannel.LoginInfo;
import com.gotye.api.voichannel.MemberType;
import com.gotye.api.voichannel.TalkMode;
import com.gotye.api.voichannel.VoiChannelAPIListener;

public class GotyeChatRoomActivity extends Activity implements
		PlatformActionListener {
	public static final String TAG = "GotyeChatRoomActivity";
	private GridView mGridView;
	private String mTitle;
	private String mCurrentChannel = null;
	private AudioManager mAudioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gotye);

		adjustUI();

		// loginButton = (Button) findViewById(R.id.LoginButton);
		// joinChannelButton = (Button) findViewById(R.id.JoinButton);
		exitChannelButton = (Button) findViewById(R.id.exitChannelButton);

		settalkmodeButton = (Button) findViewById(R.id.SetTalkModeButton);

		// loginButton.setOnClickListener(loginButtonClickEvent);
		// joinChannelButton.setOnClickListener(joinChannelButtonClickEvent);
		exitChannelButton.setOnClickListener(exitChannelBunntonEvent);
		leftMuteButton.setOnClickListener(speakOutClickEvent);
		speakButton.setOnClickListener(speakButtonClickEvent);
		muteButton.setOnClickListener(selfMuteButtonClickEvent);
		settalkmodeButton.setOnClickListener(settalkmodeButtonClickEvent);

		appIDEdit = (EditText) findViewById(R.id.editTextAppID);
		userIDEdit = (EditText) findViewById(R.id.editTextUserID);
		nickNameEdit = (EditText) findViewById(R.id.editTextNickname);
		channelIDEdit = (EditText) findViewById(R.id.editTextChanneID);
		channelPwdEdit = (EditText) findViewById(R.id.editTextChannePWD);
		channelNameEdit = (EditText) findViewById(R.id.editTextChanneName);
		userPwdEdit = (EditText) findViewById(R.id.editTextUserPwd);
		talkModeText = (TextView) findViewById(R.id.textChannelStateView);

		// Random rdm = new Random(System.currentTimeMillis());
		// int Rd = Math.abs(rdm.nextInt()) % 1000 + 1; // 获取随机数
		// userIDEdit.setText(Integer.toString(Rd)); //设置默认的UserID
		// (一个UserID对应一个用户)
		// nickNameEdit.setText("玩家" + Integer.toString(Rd)); //设置默认的用户昵称
		// channelIDEdit.setText("3"); //设置用户默认登录的频道（默认的频道ID为3）
		UserManager manager = UserManager.getInstance();

		String name = manager.getUser().name;
		Log.e(TAG, "name = " + name);

		String phone = StringUtil.formatPhone(manager.getUser().phone);
		Log.e(TAG, "phone = " + phone);
		GotyeLoginInfoManager.getInstance()
				.setUsername(phone);
		GotyeLoginInfoManager.getInstance().setNickname(name);
		

		appKey = GotyeLoginInfoManager.getInstance().getAppKey();
		GotyeLoginInfoManager.getInstance().getVoichannelapi()
				.init(GotyeChatRoomActivity.this, appKey);
		// GotyeLoginInfoManager.getInstance().getVoichannelapi().setHostAndPort("192.168.1.14",
		// 6060, 6443);

		if (mGridView == null) {
			mGridView = (GridView) findViewById(R.id.app_grid);
		}
		mGridView.setVisibility(View.VISIBLE);
		// mGridView
		// .setAdapter(new ServiceZoneAdapter(ServiceZone.this, mService));
		// mGridView.setOnItemClickListener(this);
		// if (mSelectionPosition < 0) {
		// mSelectionPosition = 0;
		// }
		// mGridView.setSelection(mSelectionPosition);

		list = (ListView) findViewById(R.id.ListView01);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				list.setTag(arg2);
				list.showContextMenu();
				return true;
			}
		});

		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {

				MenuItem item = null;
				Map<String, Object> member = GotyeLoginInfoManager
						.getInstance().getUserList()
						.get((Integer) list.getTag());
				final String userId = (String) member.get("UserId");
				boolean bSilence = false;
				if (member.containsKey("Silence")) {
					bSilence = (Boolean) (member.get("Silence"));
				}

				if (bSilence) { // 该用户已被禁言
					item = menu.add(0, 0, 0, "Undo Forbidden");
					item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							GotyeLoginInfoManager.getInstance()
									.getVoichannelapi().unsilence(userId);
							return false;
						}
					});
				} else {
					item = menu.add(0, 0, 0, "Forbid");
					item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							GotyeLoginInfoManager.getInstance()
									.getVoichannelapi().silence(userId);
							return false;
						}
					});
				}

				boolean bManager = false;
				if (member.containsKey("MemberType")) {
					MemberType type = (MemberType) member.get("MemberType");
					if (type == MemberType.Administrator) {
						item = menu.add(0, 1, 0, "Demote");
						bManager = true;
					} else if (type == MemberType.Common) {
						item = menu.add(0, 1, 0, "Elevate");
					} else {

						return;
					}
				} else {
					item = menu.add(0, 1, 0, "Elevate");
				}

				if (bManager) { // 该用户是管理员
					item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							GotyeLoginInfoManager.getInstance()
									.getVoichannelapi().demote(userId);
							return false;
						}
					});
				} else {
					item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							GotyeLoginInfoManager.getInstance()
									.getVoichannelapi().elevate(userId);
							return false;
						}
					});
				}
			}
		});

		GotyeLoginInfoManager.getInstance().setmProxy(listener);
		// updateUI();

	}

	private void adjustUI() {
		// title
		TextView chatRoomTitle = (TextView) findViewById(R.id.chat_room_title);
		Bundle bundle = getIntent().getExtras();
		mTitle = bundle.getString("title");
		if (TextUtils.isEmpty(mTitle)) {
			chatRoomTitle.setText("聊天室");
		} else {
			chatRoomTitle.setText(mTitle);
		}
		adjustUnitSize(chatRoomTitle);
		adjustFontSize(chatRoomTitle);

		mCurrentChannel = bundle.getString("roomId");

		ImageView back = (ImageView) findViewById(R.id.button_back);
		adjustUnitSize(back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.CHATROOMS);
				finish();
			}
		});
		final LinearLayout marker_layout = (LinearLayout) findViewById(R.id.marker_layout);
		TextView share = (TextView) findViewById(R.id.button_share);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.VISIBLE);
			}
		});

		adjustUnitSize(share);
		adjustFontSize(share);

		RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.title_bar);
		adjustUnitSize(titleBar);

		TextView marker_layout_cancel = (TextView) findViewById(R.id.marker_layout_cancel);
		adjustUnitSize(marker_layout_cancel);
		adjustFontSize(marker_layout_cancel);
		marker_layout_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.GONE);
			}
		});

		ImageView image_wechatmoments = (ImageView) findViewById(R.id.image_wechatmoments);
		image_wechatmoments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.GONE);
				share(WechatMoments.NAME);
			}
		});

		ImageView image_wechat = (ImageView) findViewById(R.id.image_wechat);
		image_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.GONE);
				share(Wechat.NAME);
			}
		});

		leftMuteButton = (ImageView) findViewById(R.id.speakout);
		speakButton = (ImageView) findViewById(R.id.SpeakButton);
		muteButton = (ImageView) findViewById(R.id.SelfMuteButton);

		// adjustWidth(leftMuteButton, 60);
		// adjustWidth(speakButton, 60);
		// adjustWidth(muteButton, 60);
	}

	/**
	 * 分享
	 *
	 * @param platformName
	 *            平台名称
	 */
	private void share(String platformName) {
		if (TextUtils.isEmpty(mCurrentChannel) || TextUtils.isEmpty(mTitle)) {
			Toast.makeText(GotyeChatRoomActivity.this, "请先点击发言按钮",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 参数
		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		Bitmap imageData = BitmapFactory.decodeResource(getResources(),
				R.drawable.dark_logo);
		Bitmap resizeBmp = small(imageData);
		sp.setImageData(resizeBmp);
		// sp.setImageUrl(mCoverBigUrl);
		sp.setTitle("快来一起聊天吧");

		String title_encode = null;
		try {
			title_encode = URLEncoder.encode(mTitle, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String urlEncode = "http://www.ddicar.com/Room/Room.html?roomId="
				+ mCurrentChannel + "&name=" + title_encode;
		sp.setUrl(urlEncode);
		// sp.setTitleUrl(mShareUrl);

		sp.setText("邀请你加入房间: " + mTitle);
		// 平台
		Platform platFriends = ShareSDK.getPlatform(
				this.getApplicationContext(), platformName);
		platFriends.setPlatformActionListener(this);
		platFriends.share(sp);
	}
	
  private  Bitmap small(Bitmap bitmap) {
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(0.83f,0.7f); //长和宽放大缩小的比例
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
		 }
	
//	public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//    // 原始图片的宽高
//    final int height = options.outHeight;
//    final int width = options.outWidth;
//    int inSampleSize = 1;
// 
//    if (height > reqHeight || width > reqWidth) {
// 
//        final int halfHeight = height / 2;
//        final int halfWidth = width / 2;
// 
//        // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
//        while ((halfHeight / inSampleSize) > reqHeight
//                && (halfWidth / inSampleSize) > reqWidth) {
//            inSampleSize *= 2;
//        }
//    }
// 
//    return inSampleSize;
//}
//	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//	        int reqWidth, int reqHeight) {
//	 
//	    // 首先设置 inJustDecodeBounds=true 来获取图片尺寸
//	    final BitmapFactory.Options options = new BitmapFactory.Options();
//	    options.inJustDecodeBounds = true;
//	    BitmapFactory.decodeResource(res, resId, options);
//	 
//	    // 计算 inSampleSize 的值
//	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//	 
//	    // 根据计算出的 inSampleSize 来解码图片生成Bitmap
//	    options.inJustDecodeBounds = false;
//	    return BitmapFactory.decodeResource(res, resId, options);
//	}

	protected void adjustWidth(View view, int adjustment) {
		int width = AndroidUtil.pixel(adjustment);

		LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	private void adjustUnitSize(View view) {
		LayoutParams params = view.getLayoutParams();
		params.height = unitHeight();
		view.setLayoutParams(params);
	}
	
	private int unitHeight() {
		return AndroidUtil.pixel(45);
	}
	
	private void adjustFontSize(TextView view) {
		view.setTextSize(AndroidUtil.dependentFontSize(20));
	}

	@Override
	protected final void onDestroy() {
		super.onDestroy();
		GotyeLoginInfoManager.getInstance().getVoichannelapi().exit();
	}

	private void startTalking() {
		GotyeLoginInfoManager.getInstance().getVoichannelapi().startTalking();
		if (voiceTrafficTimer != null) {
			return;
		}

		// TODO
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						GotyeLoginInfoManager.getInstance().setSendTraffic(
								GotyeLoginInfoManager.getInstance()
										.getVoichannelapi()
										.getCurrentVoiceTrafficSend());
						listItemAdapter.notifyDataSetChanged();
					}
				});
			}
		};
		voiceTrafficTimer = new Timer();
		voiceTrafficTimer.schedule(task, new Date(), 1000);
	}

	private void stopTalking() {
		GotyeLoginInfoManager.getInstance().getVoichannelapi().stopTalking();

		if (voiceTrafficTimer != null) {
			voiceTrafficTimer.cancel();
			voiceTrafficTimer = null;
		}
	}

	public class Listener implements VoiChannelAPIListener {
		// @Override
		// public void onLogin(boolean success)
		// {
		// if(isFinishing()){
		// return;
		// }
		// //用户登录语音服务器成功
		// loginButton.setText("退出");
		// }

		@Override
		public void onExit(boolean success) {
			if (isFinishing()) {
				return;
			}
			// 用户退出语音服务器

			Toast.makeText(GotyeChatRoomActivity.this,
					"User Logout: " + success, Toast.LENGTH_SHORT).show();

			// loginButton.setText("登录");

			// joinChannelButton.setText("登录频道");

			// muteButton.setText("Mute");

			// speakButton.setText("Start Talk");

			talkModeText.setText("");

			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
		}

		@Override
		public void onJoinChannel(boolean success) {
			if (isFinishing()) {
				return;
			}

			// TODO 自动加入频道 自动跳转到onresume loading.show loading.dismiss

			// 用户加入频道成功
			// Toast.makeText(GotyeChatRoomActivity.this,
			// "Channel Joined: " + success, Toast.LENGTH_SHORT).show();
			// joinChannelButton.setText("退出频道");
			channelIDEdit.setText(""
					+ GotyeLoginInfoManager.getInstance().getChannelID());
		}

		@Override
		public void onGetChannelMember(String userId) {
			Log.e(TAG, "onGetChannelMember");
			if (isFinishing()) {
				return;
			}
			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
			GotyeLoginInfoManager.getInstance().getVoichannelapi()
					.requestUserNickname(new String[] { userId });
		}

		@Override
		public void onExitChannel(boolean success) {
			if (isFinishing()) {
				return;
			}// 用户成功退出当前频道
			Toast.makeText(GotyeChatRoomActivity.this,
					"Channel Exited: " + success, Toast.LENGTH_SHORT).show();

			// joinChannelButton.setText("登录频道");

			// speakButton.setText("Start Talk");

			talkModeText.setText("");
			channelIDEdit.setText("");

			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();

		}

		@Override
		public void onRemoveChannelMember(String userId) {
			if (isFinishing()) {
				return;
			}
			// 频道的其他成员退出当前频道或下线
			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
		}

		@Override
		public void onSilencedStateChanged(boolean silenced, String userId) {
			if (isFinishing()) {
				return;
			}
			// 频道成员禁言或被取消禁言
			// boolean bFind = false ;
			// for(Map<String, Object> member :
			// GotyeLoginInfoManager.getInstance().getUserList())
			// {
			// String memberIdString = (String)(member.get("UserId"));
			// if(memberIdString.equals(userId))
			// {
			// member.put("Silence", silenced);
			// bFind = true ;
			// break;
			// }
			// }
			//
			// if(bFind)
			// {
			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
			// }
		}

		@Override
		public void onMuteStateChanged(boolean muted) {
			if (isFinishing()) {
				return;
			}
			// 禁用或恢复自己的麦克风和扬声器
			// if (muted) {
			// muteButton.setText("Unmute");
			// } else {
			// muteButton.setText("Mute");
			// }

		}

		@Override
		public void onStartTalking(String userId) {
			Log.e("VoiceChannel", "onStartTalking " + userId);

			if (isFinishing()) {
				return;
			}
			// boolean bFind = false;
			// for (Map<String, Object> member :
			// GotyeLoginInfoManager.getInstance().getUserList()) {
			// String memberIdString = (String) (member.get("UserId"));
			// if (memberIdString.equals(userId)) {
			// member.put("SpeakState", true);
			// bFind = true;
			// break;
			// }
			// }
			//
			// if (bFind) {
			// //频道成员在说话
			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
			// }
		}

		@Override
		public void onStopTalking(String userId) {
			Log.d("VoiceChannel", "onStopTalking " + userId);

			if (isFinishing()) {
				return;
			}
			// boolean bFind = false;
			// for (Map<String, Object> member :
			// GotyeLoginInfoManager.getInstance().getUserList()) {
			// String memberIdString = (String) (member.get("UserId"));
			// if (memberIdString.equals(userId)) {
			// member.put("SpeakState", false);
			// bFind = true;
			// break;
			// }
			// }

			// if (bFind) {
			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
			// }
		}

		@Override
		public void notifyChannelMemberTypes(Map<String, MemberType> typeMap) {
			Log.e(TAG, "notifyChannelMemberTypes " + typeMap);
			if (isFinishing()) {
				return;
			}

			// listItemAdapter.notifyDataSetChanged();
			// closeContextMenu();
			for (Map<String, Object> member : GotyeLoginInfoManager
					.getInstance().getUserList()) {
				Log.e(TAG, "member: " + member);
				// String memberIdString = (String) (member.get("UserId"));
				// if (typeMap.containsKey(memberIdString)) {
				// member.put("MemberType", typeMap.get(memberIdString));
				// } else {
				// member.put("MemberType", MemberType.Common);
				// }

				listItemAdapter.notifyDataSetChanged();
				closeContextMenu();
			}

		}

		@Override
		public void notifyChannelTalkMode(TalkMode talkMode) {
			Log.e(TAG, "notifyChannelTalkMode " + talkMode);

			if (isFinishing()) {
				return;
			}
			// 频道的发言模式
			talkModeText.setText(GotyeLoginInfoManager.getInstance().getMode());
		}

		@Override
		public void onError(ErrorType errorType) {
			if (isFinishing()) {
				return;
			}
			// 操作的异常信息
			ErrorType errortype = (ErrorType) errorType;
			if (errortype == ErrorType.ErrorAppNotExsit) {
				Toast.makeText(getApplicationContext(), "应用不存在",
						Toast.LENGTH_SHORT).show();
			} else if (errortype == ErrorType.ErrorChannelIsFull) {
				Toast.makeText(getApplicationContext(), "频道已经满了",
						Toast.LENGTH_SHORT).show();

			} else if (errortype == ErrorType.ErrorInvalidUserID) {
				Toast.makeText(getApplicationContext(), "无效的用户ID",
						Toast.LENGTH_SHORT).show();
			} else if (errortype == ErrorType.ErrorUserIDInUse) {
				Toast.makeText(getApplicationContext(), "用户ID已经登录了",
						Toast.LENGTH_SHORT).show();
			} else if (errortype == ErrorType.ErrorNetworkInvalid) {

				// loginButton.setText("登录");

				// joinChannelButton.setText("登录频道");

				// muteButton.setText("Mute");

				// speakButton.setText("Start Talk");
				// talkModeText.setText("");

				listItemAdapter.notifyDataSetChanged();
				closeContextMenu();
				Toast.makeText(getApplicationContext(), "网络已经断开",
						Toast.LENGTH_SHORT).show();
			} else if (errortype == ErrorType.ErrorServerIsFull) {
				Toast.makeText(getApplicationContext(), "服务器已经满了",
						Toast.LENGTH_SHORT).show();
			} else if (errortype == ErrorType.ErrorPermissionDenial) {
				Toast.makeText(getApplicationContext(), "权限被拒绝",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "发生网络错误",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onGetUserNickname(Map<String, String> userMap) {
			
			Iterator<String> keys = userMap.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				String value = userMap.get(key);
				Log.e(TAG , "user.nick_name = " + value);
				
			}
			listItemAdapter.notifyDataSetChanged();
			closeContextMenu();
		}

		@Override
		public void onChannelRemoved() {
			Toast.makeText(GotyeChatRoomActivity.this,
					"Channel has been removed", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onGetChannelDetail(ChannelInfo arg0) {
			if (arg0 != null) {
				channelNameEdit.setText(arg0.getName());
				Log.e(TAG, arg0.getName());
			}
		}

		public void updateUserList(List<Map<String, Object>> userList) {
			listItemAdapter.updateUserList(userList);
		}
	};

	private Listener listener = new Listener();
	// private Button loginButton;
	// private Button joinChannelButton;
	private Button exitChannelButton;

	private ImageView leftMuteButton;
	private ImageView speakButton;
	private ImageView muteButton;
	private Button settalkmodeButton;

	private EditText appIDEdit;
	private EditText userIDEdit;
	private EditText nickNameEdit;
	private EditText channelIDEdit;
	private EditText channelPwdEdit;
	private EditText userPwdEdit;
	private EditText channelNameEdit;

	private TextView talkModeText;

	private String nickname = "";
	private String userId = "";
	private String appKey = "";
	private LoginInfo info = new LoginInfo();
	// static private boolean bSelfMute = false;
	// static private boolean bLogin = false;
	// static private boolean bJoin = false;
	// static private boolean bSpeakMode = false;
	// static private String channelID = "";
	// private boolean bChannelTalkMode = false;

	private ImageAdapter listItemAdapter;
	// private SimpleAdapter listItemAdapter;

	private ListView list;

	private Timer voiceTrafficTimer;

	@Override
	protected void onResume() {
		super.onResume();
		updateUI();
		if (!GotyeLoginInfoManager.getInstance().isInChannel()) {
			Log.e(TAG, "not in channel");
			joinChannel();
			speakButton.setImageResource(R.drawable.click_speak);
			return;
		}
	}

	public void updateUI() {

		if (GotyeLoginInfoManager.getInstance().isOnline()) {
			// loginButton.setText("退出");
		} else {
			// loginButton.setText("登录");
		}

		// if(GotyeLoginInfoManager.getInstance().isInChannel())
		// {
		// joinChannelButton.setText("退出频道");
		// }else{
		// joinChannelButton.setText("加入频道");
		// }

		// if (GotyeLoginInfoManager.getInstance().isMute()) {
		// muteButton.setText("Unmute");
		// } else {
		// muteButton.setText("Mute");
		// }

		// if (GotyeLoginInfoManager.getInstance().isSpeak()) {
		// speakButton.setText("Stop Talk");
		// } else {
		// speakButton.setText("Start Talk");
		// }

		mAudioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		// audioManager.setMode(AudioManager.ROUTE_SPEAKER);
		mAudioManager.setMode(AudioManager.MODE_IN_CALL);
		// currVolume = audioManager
		// .getStreamVolume(AudioManager.STREAM_VOICE_CALL);

		if (!mAudioManager.isSpeakerphoneOn()) {
			mAudioManager.setSpeakerphoneOn(true);
			mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
					mAudioManager
							.getStreamVolume(AudioManager.STREAM_VOICE_CALL),
					AudioManager.STREAM_VOICE_CALL);
			leftMuteButton.setImageResource(R.drawable.hf_on);
		}

		talkModeText.setText(GotyeLoginInfoManager.getInstance().getMode());

		appIDEdit.setText("" + GotyeLoginInfoManager.getInstance().getAppKey());
		String username = GotyeLoginInfoManager.getInstance().getUsername();
		String nickname2 = GotyeLoginInfoManager.getInstance().getNickname();
		
		userIDEdit.setText("" + nickname2);		//TODO depends on the logged in user.
		nickNameEdit.setText("" + nickname2);
		
		Log.e(TAG, "username = " + username);
		Log.e(TAG, "nickname2 = " + nickname2);

		channelIDEdit.setText(""
				+ GotyeLoginInfoManager.getInstance().getChannelID());

		// 生成适配器的Item和动态数组对应的元素
		if (listItemAdapter == null) {
			// listItemAdapter = new SimpleAdapter(this, GotyeLoginInfoManager
			// .getInstance().getUserList(),
			// R.layout.list_item_gotye_member,
			// new String[] { "Information" },
			// new int[] { R.id.textInformation });
			// list.setAdapter(listItemAdapter);
			listItemAdapter = new ImageAdapter(this);
			mGridView.setAdapter(listItemAdapter);
		}
		listItemAdapter.notifyDataSetChanged();
		closeContextMenu();
	}

	// private final OnClickListener loginButtonClickEvent = new
	// OnClickListener() {
	// public void onClick(final View v) {
	// if(!GotyeLoginInfoManager.getInstance().isOnline())
	// {
	// appKey = (appIDEdit).getText().toString().trim();
	// userId = (userIDEdit).getText().toString().trim();
	// nickname = (nickNameEdit).getText().toString().trim();
	// GotyeLoginInfoManager.getInstance().getVoichannelapi().init(testMainview.this,
	// appKey);
	// info.setUserId(userId);
	// info.setNickname(nickname);
	//
	// GotyeLoginInfoManager.getInstance().setAppKey(appKey);
	// GotyeLoginInfoManager.getInstance().setUsername(userId);
	// GotyeLoginInfoManager.getInstance().setNickname(nickname);
	//
	// String channelInfo = "2@192.168.1.62:64738";
	// GotyeLoginInfoManager.getInstance().getVoichannelapi().setLoginInfo(info);
	// GotyeLoginInfoManager.getInstance().getVoichannelapi().joinChannel(channelInfo);
	// }
	// else {
	// GotyeLoginInfoManager.getInstance().getVoichannelapi().exit();
	// }
	// }
	// };

	private final OnClickListener exitChannelBunntonEvent = new OnClickListener() {

		@Override
		public void onClick(View v) {
			GotyeLoginInfoManager.getInstance().setChannelID("");
			GotyeLoginInfoManager.getInstance().getVoichannelapi()
					.exitChannel();
		}
	};

	private final OnClickListener joinChannelButtonClickEvent = new OnClickListener() {
		public void onClick(final View v) {
			joinChannel();
		}
	};

	private void joinChannel() {
		// if(!GotyeLoginInfoManager.getInstance().isInChannel())
		// {
		// String channelID = (channelIDEdit).getText().toString().trim();
		// if(channelID.equals(""))
		// {
		// return;
		// }
		//
		// Pattern pattern = Pattern.compile("[0-9]*");
		// if( pattern.matcher(channelID).matches())
		// {
		userId = (userIDEdit).getText().toString().trim();
		nickname = (nickNameEdit).getText().toString().trim();
		Log.e(TAG, "userId: " + userId);
		Log.e(TAG, "nickname: " + nickname);

		info.setUserId(userId);
		info.setNickname(nickname);
		info.setPassword(userPwdEdit.getText().toString());

		GotyeLoginInfoManager.getInstance().setAppKey(appKey);
		GotyeLoginInfoManager.getInstance().setUsername(userId);
		GotyeLoginInfoManager.getInstance().setNickname(nickname);
		GotyeLoginInfoManager.getInstance().getVoichannelapi()
				.setLoginInfo(info);

		// String[] channelInfo = GotyeLoginInfoManager.getInstance()
		// .getChannels();
		// int tmp = index++ % channelInfo.length;

		// String currentChannel = channelInfo[tmp];
		GotyeLoginInfoManager.getInstance().setChannelID(mCurrentChannel);
		ChannelInfo channel = new ChannelInfo(mCurrentChannel, channelPwdEdit
				.getText().toString());
		GotyeLoginInfoManager.getInstance().getVoichannelapi()
				.joinChannel(channel);
		GotyeLoginInfoManager.getInstance().getVoichannelapi()
				.requestChannelDetail(mCurrentChannel);
		// }
		// }
		// else {
		// GotyeLoginInfoManager.getInstance().getVoichannelapi().exitChannel();
		// }
	}

	private final OnClickListener selfMuteButtonClickEvent = new OnClickListener() {
		public void onClick(final View v) {
			if (!GotyeLoginInfoManager.getInstance().isMute()) {
				muteButton.setImageResource(R.drawable.voice_on);
				GotyeLoginInfoManager.getInstance().getVoichannelapi().mute();
			} else {
				muteButton.setImageResource(R.drawable.voice_off);
				GotyeLoginInfoManager.getInstance().getVoichannelapi()
						.restore();
			}
		}
	};
	private boolean isSpeakOutOpen = true;
	private final OnClickListener speakOutClickEvent = new OnClickListener() {

		public void onClick(final View v) {

			Log.e(TAG, "speak out" + isSpeakOutOpen);
			if (!isSpeakOutOpen) {
				openSpeakOut();
				isSpeakOutOpen = true;

			} else {
				CloseSpeakOut();
				isSpeakOutOpen = false;

			}
		}
	};

	private void openSpeakOut() {
		try {
			if (!mAudioManager.isSpeakerphoneOn()) {
				Log.e(TAG, "打开扬声器");
				mAudioManager.setSpeakerphoneOn(true);
				leftMuteButton.setImageResource(R.drawable.hf_on);
				mAudioManager
						.setStreamVolume(
								AudioManager.STREAM_VOICE_CALL,
								mAudioManager
										.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
								AudioManager.STREAM_VOICE_CALL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void CloseSpeakOut() {

		try {
			if (mAudioManager != null) {
				if (mAudioManager.isSpeakerphoneOn()) {
					Log.e(TAG, "关闭扬声器");
					mAudioManager.setSpeakerphoneOn(false);
					mAudioManager
							.setStreamVolume(
									AudioManager.STREAM_VOICE_CALL,
									mAudioManager
											.getStreamVolume(AudioManager.STREAM_VOICE_CALL),
									AudioManager.STREAM_VOICE_CALL);
					leftMuteButton.setImageResource(R.drawable.hf_off);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Toast.makeText(context,"揚聲器已經關閉",Toast.LENGTH_SHORT).show();
	}

	private final OnClickListener speakButtonClickEvent = new OnClickListener() {
		public void onClick(final View v) {
			if (!GotyeLoginInfoManager.getInstance().isInChannel()) {
				Log.e(TAG, "not in channel");
				joinChannel();
				speakButton.setImageResource(R.drawable.click_speak);
				return;
			}

			if (!GotyeLoginInfoManager.getInstance().isSpeak()) {
				speakButton.setImageResource(R.drawable.cancel_speak);
				Log.e(TAG, "start talking");
				startTalking();
				GotyeLoginInfoManager.getInstance().setSpeak(true);
				// speakButton.setText("Stop Talk");
			} else {
				speakButton.setImageResource(R.drawable.click_speak);
				Log.e(TAG, "stop talking");
				stopTalking();
				GotyeLoginInfoManager.getInstance().setSpeak(false);
				// speakButton.setText("Start Talk");
			}

		}
	};

	private final OnClickListener settalkmodeButtonClickEvent = new OnClickListener() {
		public void onClick(final View v) {
			if (!GotyeLoginInfoManager.getInstance().isbChannelTalkMode()) {
				GotyeLoginInfoManager.getInstance().getVoichannelapi()
						.setChannelTalkMode(TalkMode.AdministratorOnly);
			} else {
				GotyeLoginInfoManager.getInstance().getVoichannelapi()
						.setChannelTalkMode(TalkMode.Freedom);
			}

		}
	};

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

	private class ImageAdapter extends BaseAdapter {
		private Context context;
		// private final String[] mobileValues;
		private List<Map<String, Object>> _userList = new ArrayList<Map<String, Object>>();

		// public ImageAdapter(Context context, String[] mobileValues) {
		// this.context = context;
		// this.mobileValues = mobileValues;
		// }

		public ImageAdapter(Context context) {
			this.context = context;
		}

		public void updateUserList(List<Map<String, Object>> userList) {
			_userList.clear();
			for (Map<String, Object> u : userList) {
				_userList.add(u);
			}
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View gridView = null;

			Log.e("parent.getChildCount();", parent.getChildCount() + "");
			Log.e("position: ", position + "");

			if (convertView == null) {
				gridView = new View(context);
				gridView = inflater.inflate(R.layout.grid_view_item, null);
			} else {
				gridView = (View) convertView;
			}

			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);

			if (0 == position) { // 里面就是正常的position
				// if (UserManager.getInstance().getUser().phone.equals(userId))
				// {
				textView.setText("我");
				// } else {
				// textView.setText(userId);
				// }
			} else { // 临时的position=0
				Log.e(TAG, _userList.get(position).get("UserId") + "");
				String userId = (String) this._userList.get(position).get(
						"Nickname");
				textView.setText(userId);
			}
			return gridView;
		}

		@Override
		public int getCount() {
			return _userList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}
}
