package com.ddicar.melonradio.service;

import java.util.Map;

import com.gotye.api.voichannel.ChannelInfo;
import com.gotye.api.voichannel.ErrorType;
import com.gotye.api.voichannel.MemberType;
import com.gotye.api.voichannel.TalkMode;
import com.gotye.api.voichannel.VoiChannelAPIListener;

public class VoiceListener implements VoiChannelAPIListener {

	@Override
	public void notifyChannelMemberTypes(Map<String, MemberType> arg0) {
		System.out.println("notifyChannelMemberTypes:" + arg0);

	}

	@Override
	public void notifyChannelTalkMode(TalkMode arg0) {
		System.out.println("notifyChannelTalkMode:" + arg0);

	}

	@Override
	public void onChannelRemoved() {
		System.out.println("onChannelRemoved:");

	}

	@Override
	public void onError(ErrorType arg0) {
		System.out.println("onError:" + arg0);

	}

	@Override
	public void onExit(boolean arg0) {
		System.out.println("onExit:" + arg0);

	}

	@Override
	public void onExitChannel(boolean arg0) {
		System.out.println("onExitChannel:" + arg0);

	}

	@Override
	public void onGetChannelDetail(ChannelInfo arg0) {
		System.out.println("onGetChannelDetail:" + arg0);

	}

	@Override
	public void onGetChannelMember(String text) {
		System.out.println("onJoinChannel:" + text);

	}

	@Override
	public void onGetUserNickname(Map<String, String> arg0) {
		System.out.println("onGetUserNickname:" + arg0);

	}

	@Override
	public void onJoinChannel(boolean state) {
		System.out.println("onJoinChannel:" + state);

	}

	@Override
	public void onMuteStateChanged(boolean state) {
		System.out.println("onMuteStateChanged:" + state);

	}

	@Override
	public void onRemoveChannelMember(String text) {
		System.out.println("onRemoveChannelMember:" + text);

	}

	@Override
	public void onSilencedStateChanged(boolean state, String text) {
		System.out.println("onSilencedStateChanged:" + state + ":" + text);

	}

	@Override
	public void onStartTalking(String text) {
		System.out.println("onStartTalking:" + text);

	}

	@Override
	public void onStopTalking(String text) {
		System.out.println("onStopTalking:" + text);
	}

}
