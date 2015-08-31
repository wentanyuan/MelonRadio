package com.ddicar.melonradio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.gotye.api.voichannel.ChannelInfo;
import com.gotye.api.voichannel.ErrorType;
import com.gotye.api.voichannel.MemberType;
import com.gotye.api.voichannel.TalkMode;
import com.gotye.api.voichannel.VoiChannelAPI;
import com.gotye.api.voichannel.VoiChannelAPIListener;

public class GotyeLoginInfoManager {

	private static final String TAG = "GotyeLoginInfoManager";

	private static GotyeLoginInfoManager mInstance = new GotyeLoginInfoManager();

	private GotyeLoginInfoManager() {
		voichannelapi.addListener(new Listener());
	}

	public synchronized static GotyeLoginInfoManager getInstance() {
		return mInstance;
	}

	public static void init(Context context) {
		mInstance.mContext = context;
	}

	private Context mContext;
	private VoiChannelAPI voichannelapi = VoiChannelAPI.getInstance();

	// private String appKey = "516d2720-8f59-45cd-ad35-1792cf543921";
	// private String[] channels = {"10010815","10010856","10010857"};

	// change to liuyang is id
	private String appKey = "bcd28d42-eeb7-455f-b030-aa3805510f39";
	// private String[] channels = {
	// "eyJjaGFubmVsSWQiOiIyIiwicm9vbUlkIjo3OTU5NSwic2VydmVySWQiOjcxODUsInR5cGUiOjF9",
	// "eyJjaGFubmVsSWQiOiIzIiwicm9vbUlkIjo3OTU5Niwic2VydmVySWQiOjcxODUsInR5cGUiOjF9"
	// };

	private String username = "";
	private String nickname = "456";
	private String channelID = "";
	private String mode = "";

	private boolean bChannelTalkMode;

	private VoiChannelAPIListener mProxy;

	private List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();

	private boolean isOnline;

	private boolean isInChannel;

	private boolean isSpeak;

	private boolean isMute;
	private long sendTraffic;

	public static GotyeLoginInfoManager getmInstance() {
		return mInstance;
	}

	public static void setmInstance(GotyeLoginInfoManager mInstance) {
		GotyeLoginInfoManager.mInstance = mInstance;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<Map<String, Object>> getUserList() {
		return userList;
	}

	public void setUserList(List<Map<String, Object>> userList) {
		this.userList = userList;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isInChannel() {
		return isInChannel;
	}

	public void setInChannel(boolean isInChannel) {
		this.isInChannel = isInChannel;
	}

	public boolean isSpeak() {
		return isSpeak;
	}

	public void setSpeak(boolean isSpeak) {
		this.isSpeak = isSpeak;
	}

	public boolean isMute() {
		return isMute;
	}

	public void setMute(boolean isMute) {
		this.isMute = isMute;
	}

	public void updateUser(String userId, boolean bCloseContextMenu) {
		for (Map<String, Object> member : userList) {
			String memberIdString = (String) (member.get("UserId"));
			if (memberIdString.equals(userId)) {
				String information = "";
				information = (String) member.get("Nickname");
				if (information.equals("")) {
					information = "Anonymity";
				}

				if (member.containsKey("SendTraffic")) {
					long bytes = (Long) (member.get("SendTraffic"));
					information += "   " + "[" + bytes + " bytes]";
				}

				if (member.containsKey("Silence")) {
					boolean bSilence = (Boolean) (member.get("Silence"));
					if (bSilence) {
						information += "   " + "[Forbidden]";
					}
				}

				if (member.containsKey("MemberType")) {
					MemberType type = (MemberType) member.get("MemberType");
					if (type == MemberType.Administrator)
						information += "   " + "[Admin]";
					else if (type == MemberType.President)
						information += "   " + "[Owner]";
					else {

					}
				}

				if (member.containsKey("SpeakState")) {
					boolean bSpeaking = (Boolean) member.get("SpeakState");
					if (bSpeaking) {
						information += "   " + "[Talking]";
					}
				}

				member.put("Information", information);
				break;
			}
		}

	}

	void removeUser(String userId) {
		for (Map<String, Object> member : userList) {
			String memberIdString = (String) (member.get("UserId"));
			if (memberIdString.equals(userId)) {
				userList.remove(member);
				break;
			}
		}

	}

	public void addUser(String userId, String nickName) {
		Map<String, Object> member = new HashMap<String, Object>();

		member.put("UserId", userId);
		member.put("Nickname", nickName);
		member.put("Silence", false);
		// String information = "";
		// information = userinfo.nickname;

		// if (information.equals("")) {
		// information = "�ǳ�δ����";
		// }
		//
		// if (userinfo.silenced) {
		// information += "   " + "[����]";
		// }
		//
		member.put("Information", "");
		userList.add(member);

	}

	public void addSelf() {
		Map<String, Object> member = new HashMap<String, Object>();

		member.put("UserId", username);
		member.put("Nickname", nickname);
		String information = "";
		information = nickname;

		member.put("Information", information);
		userList.add(member);
	}

	public VoiChannelAPIListener getmProxy() {
		return mProxy;
	}

	public void setmProxy(VoiChannelAPIListener mProxy) {
		this.mProxy = mProxy;
	}

	public VoiChannelAPI getVoichannelapi() {
		return voichannelapi;
	}

	public boolean isbChannelTalkMode() {
		return bChannelTalkMode;
	}

	public void setbChannelTalkMode(boolean bChannelTalkMode) {
		this.bChannelTalkMode = bChannelTalkMode;
	}

	/**
	 * @return the sendTraffic
	 */
	public long getSendTraffic() {
		return sendTraffic;
	}

	// public String[] getChannels() {
	// return channels;
	// }

	/**
	 * @param sendTraffic
	 *            the sendTraffic to set
	 */
	public void setSendTraffic(long sendTraffic) {
		this.sendTraffic = sendTraffic;

		for (final Map<String, Object> member : userList) {
			String memberIdString = (String) (member.get("UserId"));
			if (!memberIdString.equals(username)) {
				continue;
			}

			if (sendTraffic > 0) {
				member.put("SendTraffic", sendTraffic);
			} else {
				member.remove("SendTraffic");
			}

			updateUser(username, true);
		}

	}

	class Listener implements VoiChannelAPIListener {
		// @Override
		// public void onLogin(boolean success) { // �û���¼�����������ɹ�
		// Globals.logError(this, "login in  successfully.");
		// isOnline = true;
		// mProxy.onLogin(success);
		// }

		@Override
		public void onExit(boolean success) { // �û��˳�����������
			Log.d(TAG, "logout");
			isOnline = false;

			isInChannel = false;

			isMute = false;

			isSpeak = false;

			mode = "";

			userList.clear();

			mProxy.onExit(success);
		}

		@Override
		public void onJoinChannel(boolean success) { // �û�����Ƶ���ɹ�
			Log.d(TAG, "join in channel  successfully.");
			userList.clear();

			addSelf();
			isOnline = true;
			isInChannel = true;

			mProxy.onJoinChannel(success);

		}

		@Override
		public void onGetChannelMember(String userId) { // �û�����Ƶ���ɹ�������Ƶ���ĳ�Ա��Ϣ(�Լ�����)
			Log.d(TAG, "onGetChannelMember " + userId);
			addUser(userId, nickname);

			mProxy.onGetChannelMember(userId);
		}

		@Override
		public void onExitChannel(boolean success) { // �û��ɹ��˳���ǰƵ��
			Log.d(TAG, " exit channel");
			isInChannel = false;

			isSpeak = false;

			userList.clear();

			mode = "";

			mProxy.onExitChannel(success);
		}

		@Override
		public void onRemoveChannelMember(String userId) { // Ƶ���������Ա�˳���ǰƵ��������
			Log.d(TAG, userId + " exit channel");
			removeUser(userId);

			mProxy.onRemoveChannelMember(userId);
		}

		@Override
		public void onSilencedStateChanged(boolean silenced, String userId) { // Ƶ����Ա���Ի�ȡ�����
			if (silenced) {
				Log.d(TAG, userId + " is Silenced");
			} else {
				Log.d(TAG, userId + " is unSilenced");
			}

			boolean bFind = false;
			for (Map<String, Object> member : userList) {
				String memberIdString = (String) (member.get("UserId"));
				if (memberIdString.equals(userId)) {
					member.put("Silence", silenced);
					bFind = true;
					break;
				}
			}

			if (bFind) {
				updateUser(userId, true);
			}

			mProxy.onSilencedStateChanged(silenced, userId);
		}

		@Override
		public void onMuteStateChanged(boolean muted) { // ���û�ָ��Լ�����˷��������
			if (muted) {
				Log.d(TAG, " Self muted");
				isMute = true;
			} else {
				Log.d(TAG, " Self unMuted");
				isMute = false;
			}

			mProxy.onMuteStateChanged(muted);
		}

		@Override
		public void onStartTalking(String userId) { // Ƶ����Ա��˵��
			Log.d(TAG, userId + " is talking");

			boolean bFind = false;
			for (final Map<String, Object> member : userList) {
				String memberIdString = (String) (member.get("UserId"));
				if (memberIdString.equals(userId)) {
					member.put("SpeakState", true);
					bFind = true;
					break;
				}
			}

			if (bFind) {
				updateUser(userId, false);
			}

			mProxy.onStartTalking(userId);
		}

		@Override
		public void onStopTalking(String userId) { // Ƶ����Աֹͣ˵��
			Log.d(TAG, userId + " stop talking");

			boolean bFind = false;
			for (Map<String, Object> member : userList) {
				String memberIdString = (String) (member.get("UserId"));
				if (memberIdString.equals(userId)) {
					member.put("SpeakState", false);
					bFind = true;
					break;
				}
			}

			if (bFind) {
				updateUser(userId, false);
			}

			mProxy.onStopTalking(userId);
		}

		@Override
		public void notifyChannelMemberTypes(Map<String, MemberType> typeMap) { // Ƶ�������г�Ա�ĳ�Ա���ͣ����map��û�д˳�Ա������Ϊ��ͨ��Ա
			for (Map<String, Object> member : userList) {
				String memberIdString = (String) (member.get("UserId"));
				Log.e(TAG, "notifyChannelMemberTypes" + memberIdString);
				if (typeMap.containsKey(memberIdString)) {
					member.put("MemberType", typeMap.get(memberIdString));
				}
				Log.e(TAG, "notifyChannelMemberTypes" + memberIdString);
				updateUser(memberIdString, true);
			}

			((GotyeChatRoomActivity.Listener) mProxy).updateUserList(userList);
			mProxy.notifyChannelMemberTypes(typeMap);
		}

		@Override
		public void notifyChannelTalkMode(TalkMode talkMode) { // Ƶ���ķ���ģʽ
			String string = "";
			if (talkMode == TalkMode.AdministratorOnly) {
				string = "Admin Only";
				bChannelTalkMode = true;
			} else if (talkMode == TalkMode.Freedom) {
				string = "Free Talking";
				bChannelTalkMode = false;
			}

			mode = string;

			mProxy.notifyChannelTalkMode(talkMode);
		}

		@Override
		public void onError(ErrorType errorType) { // �������쳣��Ϣ
			ErrorType errortype = (ErrorType) errorType;
			if (errortype == ErrorType.ErrorAppNotExsit) {
				Log.e(TAG, "App Not Exsit ");
			} else if (errortype == ErrorType.ErrorChannelIsFull) {
				Log.e(TAG, "Channel Is Full ");
			} else if (errortype == ErrorType.ErrorInvalidUserID) {
				Log.e(TAG, "Invalid UserID ");
			} else if (errortype == ErrorType.ErrorUserIDInUse) {
				Log.e(TAG, "UserID in use ");
			} else if (errortype == ErrorType.ErrorNetworkInvalid) {
				Log.e(TAG, "Invalid Network ");

				isOnline = false;

				isInChannel = false;

				isMute = false;

				isSpeak = false;

				userList.clear();
			} else if (errortype == ErrorType.ErrorServerIsFull) {
				Log.e(TAG, "Server Is Full ");
			} else if (errortype == ErrorType.ErrorPermissionDenial) {
				Log.e(TAG, "Permission Denial ");
			} else {
			}

			mProxy.onError(errorType);
		}

		@Override
		public void onGetUserNickname(Map<String, String> userMap) {

			Log.e(TAG, "onGetUserNickname");

			for (Map<String, Object> member : userList) {
				String memberId = (String) (member.get("UserId"));
				String nickname = userMap.get(memberId);
				if (nickname != null) {
					member.put("Nickname", nickname);
					updateUser(memberId, true);
					break;
				}
			}

			mProxy.onGetUserNickname(userMap);
		}

		@Override
		public void onChannelRemoved() {
			mProxy.onChannelRemoved();
		}

		@Override
		public void onGetChannelDetail(ChannelInfo arg0) {
			mProxy.onGetChannelDetail(arg0);
		}
	};
}
